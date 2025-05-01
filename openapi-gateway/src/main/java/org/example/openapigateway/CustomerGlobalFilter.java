package org.example.openapigateway;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.javassist.bytecode.BadBytecode;
import org.example.openapiclientsdk.Utils.SignUtils;
import org.openpai.common.model.entity.InterfaceInfo;
import org.openpai.common.model.entity.User;
import org.openpai.common.service.InnerInterfaceInfoService;
import org.openpai.common.service.InnerUserInterfaceInfoService;
import org.openpai.common.service.InnerUserService;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class CustomerGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    @DubboReference(timeout = 6000)
    private InnerInterfaceInfoService innerInterfaceInfoService;
    @DubboReference
    private InnerUserService innerUserService;

    @Resource
    RedisTemplate<String,String> redisTemplate;

    private static final List<String> IP_WHITE = Arrays.asList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("id"+request.getId());
        log.info("路径"+request.getPath());
        log.info("方法"+request.getMethod());
        log.info("请求参数"+request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("来源地址"+request.getRemoteAddress());
        // 黑名单
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE.contains(sourceAddress)){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();//直接完成
        }

        // 用户校验
        HttpHeaders headers = request.getHeaders();
        String appKey = headers.getFirst("appKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String path = String.valueOf(request.getPath());
        String method = String.valueOf(request.getMethod());

        User invokeUser;
        // todo 数据库
        try {
            invokeUser = innerUserService.getInvokeUser(appKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (invokeUser == null){
            handleAuth(response);
        }

        String redisKey = "openapi.nonce" + nonce;
        //System.out.println(redisKey);
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String nonceCache = valueOperations.get(redisKey);
        if ( nonceCache != null ){
            return handleAuth(response);
        }
        try {
            valueOperations.set(redisKey,nonce,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis wrong"+e);
        }

        long currentTime = System.currentTimeMillis()/1000;
        if (currentTime - Long.parseLong(timestamp) > 60 * 5){
            return handleAuth(response);
        }
        String secretCacheKey = "openapi.secretkey" + appKey;
        String skCache = valueOperations.get(secretCacheKey);
        if (skCache == null){
            skCache = invokeUser.getSecretKey();
        }
        String serverSign = SignUtils.getSign(body,skCache);
        if (!serverSign.equals(sign)){
            return handleAuth(response);
        }
        valueOperations.set(secretCacheKey,skCache,10,TimeUnit.MINUTES);


        // 模拟接口信息是否存在
        InterfaceInfo interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path,method);

        // System.out.println(interfaceInfo);// null db 接口路径与传入值不一致
        if (interfaceInfo == null){
            return handleAuth(response);
        }
        // nacos 超时 重启?

        // 检验接口是否可以调用
        // 超时
        boolean flag = innerUserInterfaceInfoService.checkInterfaceIsActive(interfaceInfo.getId(),invokeUser.getId());
        if (!flag){
            System.out.println(flag);
            return handleAuth(response);
        }
        // 请求转发,调用模拟接口
        Mono<Void> filter = chain.filter(exchange);
        log.info("response" + response.getStatusCode());
        // 响应日志
        log.info("customer global filter\n\n\n");

        return handleResponse(exchange,chain,interfaceInfo.getId(),invokeUser.getId());
    }
    /* long interfaceInfoId, long userId*/
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = (HttpStatus) originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            // 没有执行
                                            // innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }



    @Override
    public int getOrder() {
        return 0;
    }

    public Mono<Void> handleAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();//直接完成
    }
    public Mono<Void> handleInvokeError(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();//直接完成
    }
}
