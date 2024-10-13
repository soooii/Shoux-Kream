//package com.shoux_kream.item.entity;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "brand")  // 이 클래스는 'brand'라는 이름의 데이터베이스 테이블과 매핑됨
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//
//public class Brand {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 'id' 필드는 자동으로 생성되는 기본 키이며, 각 브랜드를 고유하게 식별
////    @Column(name = "id")  // 데이터베이스의 'id' 열과 매핑됨
//    private Long id;
//
//    @Column(name = "brand_title", unique = true, nullable = false)  // 데이터베이스의 'brand_name' 열과 매핑됨, 브랜드 이름을 저장 (예: Nike, Adidas)
//    private String title;
//
//    public Brand(String title) {
//        this.title = title;
//    }
//}