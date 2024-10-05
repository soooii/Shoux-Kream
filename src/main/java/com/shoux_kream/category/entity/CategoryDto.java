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

    public CategoryDto(Category category) { //엔티티 -> DTO로 변환하는 생성자
        this.id = category.getId();
        this.name = category.getName();
    }

    public Category toEntity() { //DTO -> 엔티티로 변환하는 메서드
        return new Category(name);
    }
}
