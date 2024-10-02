package com.shoux_kream.item.dto.response;

import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.entity.ItemInfo;

public record ItemResponse(
        Long id,
        BrandResponse brand,
        String name,
        ItemInfo itemInfo
) {
    public static ItemResponse fromEntity(Item item) {
        return new ItemResponse(
                item.getId(),
                BrandResponse.fromEntity(item.getBrand()),
                item.getName(),
                item.getItemInfo());
    }
}
