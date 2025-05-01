package org.example.openapiclientsdk;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.example.openapiclientsdk.Utils.SignUtils.getSign;


public class Client {
    private String appKey;
    private String secretKey;
    private Integer nonce;
    private long timestamp;
    private static final String GATEWAY_HOST = "http://localhost:8090";

    public Client(String appKey, String secretKey,Integer nonce,long timestamp) {
        this.appKey = appKey;
        this.secretKey = secretKey;
        this.nonce = nonce;
        this.timestamp = timestamp;

    }

    public String execute(String method, String url, Object body) throws Exception {
        String bodyJson = JSONUtil.toJsonStr(body);;

        HttpResponse response = switch (method) {
            case "POST" -> HttpRequest.post(GATEWAY_HOST + url)
                    .charset(StandardCharsets.UTF_8)
                    .addHeaders(getHeaderMap(bodyJson))
                    .body(bodyJson)
                    .execute();
            case "GET" -> HttpRequest.get(GATEWAY_HOST + url)
                    .charset(StandardCharsets.UTF_8)
                    .addHeaders(getHeaderMap(bodyJson))
                    //.body(bodyJson)
                    .execute();
            case "PUT" -> HttpRequest.put(GATEWAY_HOST + url)
                    .charset(StandardCharsets.UTF_8)
                    .addHeaders(getHeaderMap(bodyJson))
                    .body(bodyJson)
                    .execute();
            default -> throw new IllegalArgumentException("Unsupported method");
        };

        return response.body();

    }

    public String postName( String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String result3= HttpUtil.post(GATEWAY_HOST + "/api/name/post", paramMap);
        System.out.println(result3);
        return result3;
    }

    public String getName(String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String result3= HttpUtil.get(GATEWAY_HOST + "/api/name/get", paramMap);
        System.out.println(result3);
        return result3;
    }

    private Map<String,String> getHeaderMap(String body){
        Map<String,String> header = new HashMap<>();
        header.put("appKey",appKey);
        //header.put("secretKey",secretKey);
        header.put("nonce", String.valueOf(nonce));// 随机数应该在这生成,这里为了方便测试
        header.put("body",body);
        header.put("timestamp",String.valueOf(timestamp/1000));//应该在这生成,这里为了方便测试
        header.put("sign",getSign(body,secretKey));
        return header;
    }



    //  签名MD5 用户参数+密钥 serve 使用相同参数和算法生成签名,只要与用户传递一致
    // 重放 加nonce 随机数 时间戳
    public String getUserName(User user){
        String json = JSONUtil.toJsonStr(user);
        HttpResponse result2 = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .charset(StandardCharsets.UTF_8)
                .addHeaders(getHeaderMap(json))// 可能请求拦截 重放 不能传递密码 传递签名
                .body(json)
                .execute();
        System.out.println(result2.getStatus());
        String res = result2.body();
        System.out.println(res);
        return res;
    }
}
