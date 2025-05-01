package org.example.openapigateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * 限流配置KeyResolver——有三种写法(接口限流/ip限流/用户限流)
 */
@Configuration
public class RateLimiteConfig {

    /**
     * 接口限流：根据请求路径限流
     * 如果不使用@Primary注解，会报错
     * @return
     */
    @Bean
    @Primary
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().toString());

        /*//写法2
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                return Mono.just(exchange.getRequest().getPath().toString());
            }
        };*/
    }

    /**
     * 根据请求IP限流
     * @return
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName()
        );
    }

    /**
     * 根据请求参数中的userId进行限流
     * 请求地址写法：http://localhost:8801/rate/123?userId=lisi
     * @return
     */
    /*@Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId")
        );
    }*/
}


