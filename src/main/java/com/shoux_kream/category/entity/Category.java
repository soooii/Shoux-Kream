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
    private Long Id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String themeClass;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Category(String name, String description, String themeClass) {
        this.name = name;
        this.description = description;
        this.themeClass = themeClass;
    }


    public void updateCategory(String name, String description, String themeClass) { // 카테고리 정보 수정 메서드
        this.name = name;
        this.description = description;
        this.themeClass = themeClass;
    }
}
