package org.example.openapiclientsdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("openapi.client")
@Data
@ComponentScan
public class OpenAPIClientConfig {
    private String appKey;
    private String secretKey;
    private Integer nonce;
    private long timestamp;
    @Bean
    public Client client(){
        return new Client(appKey,secretKey,nonce,timestamp);
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
