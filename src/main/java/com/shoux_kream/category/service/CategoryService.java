package com.shoux_kream.category.service;

import com.shoux_kream.category.entity.Category;
import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private S3Service s3Service; // S3 이미지 업로드 서비스

    @Transactional // 카테고리 추가
    public CategoryDto createCategory(CategoryDto categoryDto, MultipartFile imageFile) {
        // 이미지 파일을 S3에 업로드하고 이미지 키를 받아옴
        String imageKey = s3Service.uploadFile(imageFile, "category");
        categoryDto.setImageKey(imageKey); // DTO에 이미지 키 설정

        // DTO -> 엔티티로 변환 후 저장
        Category category = categoryDto.toEntity();
        Category savedCategory = categoryRepository.save(category);

        // 저장된 카테고리를 DTO로 변환하여 반환
        return new CategoryDto(savedCategory);
    }

    @Transactional // 카테고리 전체 조회
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }


    @Transactional // 카테고리 수정
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 엔티티의 필드 업데이트
        category.updateCategory(
                categoryDto.getName(),
                categoryDto.getDescription(),
                categoryDto.getThemeClass()
        );

        // 수정된 카테고리를 DTO로 변환하여 반환
        return new CategoryDto(category);
    }

    @Transactional // 카테고리 삭제
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
