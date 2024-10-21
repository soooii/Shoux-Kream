package com.shoux_kream.category.service;

import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.entity.Category;
import com.shoux_kream.category.repository.CategoryRepository;
import com.shoux_kream.config.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final S3Uploader s3Uploader; // S3Uploader 주입;



    @Transactional // 카테고리 추가
    public CategoryDto createCategory(CategoryDto categoryDto, MultipartFile imageFile) throws IOException {
        // 이미지 파일이 존재하면 S3에 업로드
        String imageUrl = s3Uploader.upload(imageFile, "category");

        // 카테고리 생성 및 저장
        Category category = new Category(categoryDto.getTitle(), categoryDto.getDescription(), categoryDto.getThemeClass(), imageUrl);
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

    @Transactional // 특정 카테고리 조회
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryDto::new)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
    }

    @Transactional // 카테고리 수정
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto, MultipartFile imageFile) {
        // 카테고리 존재 여부 확인
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 이미지 파일이 있는 경우에만 S3에 업로드
        String imageUrl = category.getImageUrl(); // 기존 이미지 URL을 기본값으로 설정
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageUrl = s3Uploader.upload(imageFile, "categories");
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }

        // 카테고리 정보 업데이트
        category.updateCategory(categoryDto.getTitle(), categoryDto.getDescription(), categoryDto.getThemeClass(), imageUrl);

        return new CategoryDto(category);
    }


    @Transactional // 카테고리 삭제
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 카테고리가 존재하지 않습니다: " + id));
        categoryRepository.delete(category);
    }
}
