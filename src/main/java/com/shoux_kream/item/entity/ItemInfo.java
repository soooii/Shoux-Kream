package com.shoux_kream.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfo {

    @Column(name = "model_number") // 모델번호
    private String modelNumber;

    @Column(name = "release_date") // 출시일
    private LocalDateTime releaseDate;

    @Column(name = "color") // 대표 색상
    private String color;

    @Column(name = "release_price") // 발매가
    private Long releasePrice;

    // 최고가 최저가(?) 주석처리
    //        @Column(name = "highest_price", nullable = false, columnDefinition = "INT")
//        private int highestPrice;
//
//        @Column(name = "lowest_price", nullable = false, columnDefinition = "INT")
//        private int lowestPrice;
//
//        public void updateHighestPrice(int price) {
//            this.highestPrice = price;
//        }
//
//        public void updateLowestPrice(int price) {
//            this.lowestPrice = price;
}