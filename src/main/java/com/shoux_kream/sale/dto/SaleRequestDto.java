package com.shoux_kream.sale.dto;

import com.shoux_kream.sale.entity.Sale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaleRequestDto {
    private Long itemId;
    private Long sellingPrice;
    private int daysToAdd;

    public SaleRequestDto(Sale sale, int daysToAdd) {
        this.itemId = sale.getItem().getId();
        this.sellingPrice = sale.getSellingPrice();
        this.daysToAdd = daysToAdd;
    }
}
