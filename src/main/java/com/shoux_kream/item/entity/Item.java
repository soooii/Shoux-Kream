package com.shoux_kream.item.entity;

import com.shoux_kream.category.entity.Category;
import com.shoux_kream.timestamp.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@Getter
@Setter
@Entity
@Table(name = "item")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String title; // 상품명

    // Category와의 연관 관계 설정 (ManyToOne)
    // TODO 카테고리 비활성화, 10/9
//    @ManyToOne
//    @JoinColumn(name = "category_id", nullable = false) // nullable=true로 설정
//    private Category category; // 카테고리 ID

    @Column(nullable = false)
    private String manufacturer; // 제조사

    @Column(length = 255)
    private String shortDescription; // 짧은 설명

    @Column(columnDefinition = "TEXT")
    private String detailDescription; // 상세 설명

    @Column(nullable = false)
    private String imageKey; // 이미지 키 (S3에 저장된 이미지 키)

    @Column(nullable = false)
    private Integer inventory; // 재고 수량

    @Column(nullable = false)
    private Integer price; // 가격

    @OneToMany(mappedBy = "item")
    private List<KeyWord> keyWords; // 검색 키워드

    // 비활성화
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "brand_id")
//    private Brand brand;

    // 비활성화
//    @Embedded
//    private ItemInfo itemInfo;

    // Review와의 연관 관계 설정 (OneToMany)
    // 아직 리뷰 엔티티 구현 안돼서 주석처리
//    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Review> reviews;

    // 비활성화
//    @Column(name = "size")
//    private Integer size;

    //    관리자만 제품을 등록하고 관리하며, 일반 사용자는 구매만 하기 때문에,
//    제품(Item) 엔티티에 사용자(user_id)와 직접적인 연결이 필요하지 않음
//    @Column(name = "user_id", nullable = false)
//    private Integer userId;

    public Item(String title, String manufacturer, String shortDescription,
                String detailDescription, String imageKey, int inventory, int price, List<KeyWord> keyWords) {
//        this.brand = brand;
        this.title = title;
//        this.category = category;
        this.manufacturer = manufacturer;
        this.shortDescription = shortDescription;
        this.detailDescription = detailDescription;
        this.imageKey = imageKey;
        this.inventory = inventory;
        this.price = price;
        this.keyWords = keyWords;
    }

    public void update(String title, String manufacturer, String shortDescription,
                       String detailDescription, String imageKey, int inventory, int price, List<KeyWord> keyWords) {
        this.title = title;
//        this.category = category;
        this.manufacturer = manufacturer;
        this.shortDescription = shortDescription;
        this.detailDescription = detailDescription;
        this.imageKey = imageKey;
        this.inventory = inventory;
        this.price = price;
        this.keyWords = keyWords;
    }
}