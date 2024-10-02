package com.shoux_kream.item.entity;

import com.shoux_kream.category.entity.Category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;


@Getter
@Entity
@Table(name = "items")
@NoArgsConstructor(access = PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    관리자만 제품을 등록하고 관리하며, 일반 사용자는 구매만 하기 때문에,
//    제품(Item) 엔티티에 사용자(user_id)와 직접적인 연결이 필요하지 않음
//    @Column(name = "user_id", nullable = false)
//    private Integer userId;

    @Column(name = "item_name", nullable = false, length = 255)
    private String name;

    // itemInfo 에서 발매가를 관리하고 있기 때문에 삭제
//    @Column(name = "price", nullable = false)
//    private Integer price;

    // Category와의 연관 관계 설정 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "image_path", nullable = false, length = 255)
    private String imagePath;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    //일반적으로 웹사이트(크림)에서 상품의 생성시간과 수정시간을 사이트에 명시하지 않음
    //언제 구매 했는지 정보 확인은 유저란에서 하면 될 것 같음
//    @CreatedDate
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;

    // Review와의 연관 관계 설정 (OneToMany)
    // 아직 리뷰 엔티티 구현 안돼서 주석처리
//    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Review> reviews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Embedded
    private ItemInfo itemInfo;

    @Column(name = "size")
    private int size;

    //
    public Item(Brand brand, String name, ItemInfo itemInfo, int size) {
        this.brand = brand;
        this.name = name;
//        this.imagePath = imagePath;
//        this.description = description;
        this.itemInfo = itemInfo;
        this.size = size;
    }

    // 사이즈는 보통 한번 입력하고 나면 따로 수정하지 않음(신발 250 ~ 300, 옷 xs, s, m, l, xl 으로 설정)
    public void update(Brand brand, String name, String imagePath, String description, ItemInfo itemInfo) {
        this.brand = brand;
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.itemInfo = itemInfo;
    }
}