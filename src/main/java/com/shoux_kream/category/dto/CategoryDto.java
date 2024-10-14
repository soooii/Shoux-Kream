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
    private String name;
    private String imageUrl;


    public CategoryDto(Category category) { //엔티티 -> DTO로 변환하는 생성자
        this.id = category.getId();
        this.name = category.getName();
        this.imageUrl = category.getImageUrl();
    }

    public Category toEntity() { //DTO -> 엔티티로 변환하는 메서드
        return new Category(name, imageUrl);
    }
}
