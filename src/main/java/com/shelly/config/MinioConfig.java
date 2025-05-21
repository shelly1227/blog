package com.shelly.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio 配置
 * @author shelly
 */

@Configuration
public class MinioConfig {

    @Value("${upload.minio.url}")
    private String url;

    @Value("${upload.minio.access-key}")
    private String accessKey;

    @Value("${upload.minio.secret-key}")
    private String secretKey;


    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey,secretKey)
                .build();
    }
}