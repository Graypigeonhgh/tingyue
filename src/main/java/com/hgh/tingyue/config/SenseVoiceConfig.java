package com.hgh.tingyue.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenseVoiceConfig {
    @Value("${aliyun.senseVoice.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.senseVoice.accessKeySecret}")
    private String accessKeySecret;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }
}