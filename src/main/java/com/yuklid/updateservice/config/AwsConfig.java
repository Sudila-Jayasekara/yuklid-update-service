package com.yuklid.updateservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${aws.s3.endpoint-url}")
    private String endpointUrl;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }

    @Bean
    public S3Configuration s3Configuration() {
        return S3Configuration.builder()
                .pathStyleAccessEnabled(true) // This enables path-style
                .build();
    }

    @Bean
    public S3Client s3Client(AwsCredentialsProvider creds, S3Configuration s3Config) {
        return S3Client.builder()
                .credentialsProvider(creds)
                .endpointOverride(URI.create(endpointUrl))
                .serviceConfiguration(s3Config)
                .region(Region.of(region))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner(AwsCredentialsProvider creds, S3Configuration s3Config) {
        return S3Presigner.builder()
                .credentialsProvider(creds)
                .endpointOverride(URI.create(endpointUrl))
                .serviceConfiguration(s3Config)
                .region(Region.of(region))
                .build();
    }
}
