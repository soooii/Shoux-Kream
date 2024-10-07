package com.shoux_kream.review.entity;

import com.shoux_kream.item.entity.Item;
import jakarta.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "review_text", nullable = false, length = 1000)
    private String reviewText;

    @Column(name = "rating", nullable = false)
    private int rating;  // 예: 1~5점 사이의 평가 점수

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;  // 리뷰를 남긴 사용자
}
