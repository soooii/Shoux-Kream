package com.shoux_kream.item.dto.response;

import com.shoux_kream.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemResponseDto {
    private Long id;
    private String title;
    private String shortDescription;
    private String detailDescription;
    private String imageUrl;
    private Integer price;

    public SaleItemResponseDto(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.shortDescription = item.getShortDescription();
        this.detailDescription = item.getDetailDescription();
        this.imageUrl = item.getImageKey();
        this.price = item.getPrice();
    }

}
