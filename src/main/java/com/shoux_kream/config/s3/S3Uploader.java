package com.shoux_kream.config.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // 파일 이름 설정
        String fileName = dirName + "/" + multipartFile.getOriginalFilename();

        // S3에 업로드 및 URL 반환
        return uploadToS3(multipartFile, fileName);
    }

    private String uploadToS3(MultipartFile multipartFile, String fileName) throws IOException {
        // 파일의 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // S3에 파일 업로드
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata));
        String fileUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        log.info("File uploaded to S3 with URL: " + fileUrl);

        return fileUrl;
    }
}
