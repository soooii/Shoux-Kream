package com.shoux_kream.comment.repository;

import com.shoux_kream.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCategoryId(Long categoryId); // 카테고리 ID로 댓글 조회
}
