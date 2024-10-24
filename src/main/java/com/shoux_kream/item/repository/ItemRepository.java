package com.shoux_kream.item.repository;

import com.shoux_kream.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// Item 엔티티와 데이터베이스 간의 상호작용을 처리하는 리포지토리 인터페이스
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 이름으로 Item 을 검색하는 메서드, Optional 로 감싸서 값이 없을 때 NPE 를 방지
    Optional<Item> findByTitle(String title);
    Optional<Item> findByTitleContaining(String searchKeyword);

    // Item 과 브랜드 이름으로 Item 리스트를 검색하는 JPQL 쿼리
//    @Query("select i from Item i where i.title =:itemTitle and i.brand.title = :brandTitle")
//    List<Item> findAllByTitleAndBrand(@Param("itemTitle") String itemTitle, @Param("brandTitle") String brandTitle);
//
//    //TODO 상품 레파지토리 productInfo -> itemInfo 로 수정
//
//    // Item 정보를 업데이트하는 쿼리, brandId, title, color 를 업데이트함
//    // @Modifying 은 데이터 변경을 나타내며, clearAutomatically = true 는 자동으로 캐시를 정리
//    @Modifying(clearAutomatically = true)
//    @Query("update Item i " +
//            "set i.brand.id = :brandId, i.title = :title, i.itemInfo.color = :color " +
//            "where i.itemInfo.modelNumber = :modelNumber")
//    void updateItemInfo(@Param("brandId") Long brandId,
//                        @Param("title") String title,
//                        @Param("color") String color,
//                        @Param("modelNumber") String modelNumber);
}