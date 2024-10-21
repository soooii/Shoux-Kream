package com.shoux_kream.category.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shoux_kream.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "category")
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

    // 연관된 Item 목록 (OneToMany 관계로 양방향 매핑 가능)
    @OneToMany(mappedBy = "category")
    //순환참조 방지(자식)
    @JsonBackReference
    private List<Item> items;

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
