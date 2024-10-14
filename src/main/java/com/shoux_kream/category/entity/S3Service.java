package com.shoux_kream.category.entity;


import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

//이미지 업로드 서비스
@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3; // AWS S3 클라이언트

    private final String bucketName = "your-s3-bucket-name"; // S3 버킷 이름

    public String uploadFile(MultipartFile file, String folder) {
        try {
            String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), null);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }
}
