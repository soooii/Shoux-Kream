package com.shoux_kream.category.service;

import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.entity.Category;
import com.shoux_kream.category.repository.CategoryRepository;
import com.shoux_kream.config.s3.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private S3Uploader s3Uploader; // S3Uploader 주입;

    @Transactional // 카테고리 추가
    public CategoryDto createCategory(CategoryDto categoryDto, MultipartFile imageFile) {
        // 이미지 파일을 S3에 업로드하고 이미지 키를 받아옴
        try {
            // 이미지 업로드 로직
            String imageUrl = s3Uploader.upload(imageFile, "categories");
            categoryDto.setImageUrl(imageUrl);

            Category category = categoryDto.toEntity();
            Category savedCategory = categoryRepository.save(category);

            return new CategoryDto(savedCategory);
        } catch (IOException e) {
            // 예외 처리 로직
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional // 카테고리 전체 조회
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }


    @Transactional // 카테고리 수정
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto, MultipartFile imageFile) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        try {
            String imageUrl = s3Uploader.upload(imageFile, "categories"); // 이미지 업로드
            category.updateCategory(categoryDto.getName(), imageUrl); // 이름과 이미지 URL로 카테고리 업데이트
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e); // 예외 처리
        }

        return new CategoryDto(category);
    }


    @Transactional // 카테고리 삭제
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
