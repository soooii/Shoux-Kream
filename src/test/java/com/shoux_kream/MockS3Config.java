package com.shoux_kream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.shoux_kream.config.s3.S3Uploader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MockS3Config {

    @Bean
    @Primary
    public AmazonS3 amazonS3() {
        // LocalStack과 연동된 S3 클라이언트 설정
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "http://localhost:4566", "ap-northeast-2"))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials("test", "test")))
                .build();
    }

    @Bean
    public S3Uploader s3Uploader(AmazonS3 amazonS3) {
        return new S3Uploader((AmazonS3Client) amazonS3);
    }
}