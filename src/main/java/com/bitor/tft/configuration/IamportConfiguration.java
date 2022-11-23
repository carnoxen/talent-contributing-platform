package com.bitor.tft.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;

import lombok.Data;

/**
 * 아임포트
 */
@ConfigurationProperties("iamport")
@Configuration
@Data
public class IamportConfiguration {
    private String key;
    private String secret;

    public IamportClient getClient() {
        return new IamportClient(key, secret);
    }
}
