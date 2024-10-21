package com.shoux_kream.item.dto.response;

import com.shoux_kream.category.entity.Category;
import com.shoux_kream.item.entity.Item;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemResponse{
        Long id;
//        BrandResponse brand,
        String title;
        Category category;
        String manufacturer;
        String shortDescription;
        String detailDescription;
        String imageKey;
        Integer inventory;
        Integer price;
        List<String> keyWords;

        @Builder
        public ItemResponse(Long id, String title, Category category,  String manufacturer, String shortDescription, String detailDescription, String imageKey, Integer inventory, Integer price, List<String> keyWords) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.manufacturer = manufacturer;
            this.shortDescription = shortDescription;
            this.detailDescription = detailDescription;
            this.imageKey = imageKey;
            this.inventory = inventory;
            this.price = price;
            this.keyWords = keyWords;
        }

    public static ItemResponse fromEntity(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
//                .brandResponse(BrandResponse.fromEntity(item.getBrand()))
                .title(item.getTitle())
                .category(item.getCategory())
                .manufacturer(item.getManufacturer())
                .shortDescription(item.getShortDescription())
                .detailDescription(item.getDetailDescription())
                .imageKey(item.getImageKey())
                .inventory(item.getInventory())
                .price(item.getPrice())
                .keyWords(item.getKeyWords())
                .build();
    }
}
