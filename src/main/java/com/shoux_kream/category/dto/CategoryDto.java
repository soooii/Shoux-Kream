package com.shoux_kream.category.dto;

import com.shoux_kream.category.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String title;
    private String themeClass;
    private String description;
    private String imageUrl; // 이미지 URL 필드


    public CategoryDto(Category category) { //엔티티 -> DTO로 변환하는 생성자
        this.id = category.getId();
        this.title = category.getTitle();
        this.description = category.getDescription();
        this.themeClass = category.getThemeClass();
        this.imageUrl = category.getImageUrl();
    }

    public CategoryDto(String title, String description, String themeClass) {
        this.title = title;
        this.description = description;
        this.themeClass = themeClass;
    }

    public Category toEntity() { //DTO -> 엔티티로 변환하는 메서드
        return new Category(title, description, themeClass, imageUrl);
    }
}
