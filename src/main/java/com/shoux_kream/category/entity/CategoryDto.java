package com.shoux_kream.category.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String themeClass; // 테마 클래스
    private String imageKey;     // 이미지 키 (AWS S3)


    public CategoryDto(Category category) { //엔티티 -> DTO로 변환하는 생성자
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.themeClass = category.getThemeClass();
        this.imageKey = category.getImageKey();
    }

    public Category toEntity() { //DTO -> 엔티티로 변환하는 메서드
        return new Category(name, description, themeClass, imageKey);
    }
}
