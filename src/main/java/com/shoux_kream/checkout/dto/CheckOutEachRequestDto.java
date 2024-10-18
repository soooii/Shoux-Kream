package com.shoux_kream.checkout.dto;

import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.entity.CheckOutEach;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckOutEachRequestDto {
    private Long itemId;
    private int quantity;

    public CheckOutEachRequestDto(CheckOutEach checkOutEach) {
        this.itemId = checkOutEach.getItem().getId();
        this.quantity = checkOutEach.getQuantity();
    }
}
