package com.shoux_kream.item.dto.response;

import com.shoux_kream.category.entity.Category;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.entity.ItemInfo;

public record ItemResponse(
        Long id,
//        BrandResponse brand,
        String title,
//        Category category,
        String manufacturer,
        String shortDescription,
        String detailDescription,
        String imageKey,
        int inventory,
        int price,
        String searchKeywords
) {
    public static ItemResponse fromEntity(Item item) {
        return new ItemResponse(
                item.getId(),
//                BrandResponse.fromEntity(item.getBrand()),
                item.getTitle(),
//                item.getCategory(),
                item.getManufacturer(),
                item.getShortDescription(),
                item.getDetailDescription(),
                item.getImageKey(),
                item.getInventory(),
                item.getPrice(),
                item.getSearchKeywords()
        );
    }
}
