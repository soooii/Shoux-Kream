package com.shoux_kream.category.service;

import com.shoux_kream.category.entity.Category;
import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional //카테고리 추가
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryDto.toEntity();
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDto(savedCategory);
    }

    @Transactional // 카테고리 전체 조회
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }


    @Transactional //카테고리 수정
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        category.updateCategory(categoryDto.getName());
        return new CategoryDto(category);
    }

    @Transactional //카테고리 삭제
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
