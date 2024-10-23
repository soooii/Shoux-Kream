package com.shoux_kream.comment.service;

import com.shoux_kream.category.dto.CategoryDto;
import com.shoux_kream.category.entity.Category;
import com.shoux_kream.category.repository.CategoryRepository;
import com.shoux_kream.comment.dto.CommentDto;
import com.shoux_kream.comment.entity.Comment;
import com.shoux_kream.comment.repository.CommentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CommentDto createComment(CommentDto commentDto) {
        Category category = categoryRepository.findById(commentDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setCategory(category);
        Comment savedComment = commentRepository.save(comment);

        return new CommentDto(savedComment.getId(), savedComment.getContent(), savedComment.getCategory().getId());
    }

    public List<CommentDto> getCommentsByCategoryId(Long categoryId) {
        List<Comment> comments = commentRepository.findByCategoryId(categoryId);
        return comments.stream()
                .map(comment -> new CommentDto(comment.getId(), comment.getContent(), comment.getCategory().getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setContent(commentDto.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return new CommentDto(updatedComment.getId(), updatedComment.getContent(), updatedComment.getCategory().getId());

    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}

