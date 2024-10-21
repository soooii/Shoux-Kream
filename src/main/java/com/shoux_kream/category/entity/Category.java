package com.shoux_kream.category.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String description;
    private String themeClass;

    private String imageUrl; //이미지 URL 추가

    @CreatedDate // 생성 날짜
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 날짜
    private LocalDateTime updatedAt;

    public Category(String title, String description, String themeClass, String imageUrl) {
        this.title = title;
        this.description = description;
        this.themeClass = themeClass;
        this.imageUrl = imageUrl;
    }


    public void updateCategory(String title, String description, String themeClass, String imageUrl) {
        this.title = title;
        this.description = description;
        this.themeClass = themeClass;
        this.imageUrl = imageUrl;
    }
}
