package com.shoux_kream.category.repository;

import com.shoux_kream.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name); //카테고리 검색 시 해당 데이터가 없을 때 Optional 반환
}