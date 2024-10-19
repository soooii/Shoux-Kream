package com.shoux_kream.checkout.dto;

import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.entity.CheckOutEach;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutEachResponseDto {
    private Long checkOutEachId;
    private Long userId;
    private Long itemId;
    private String itemName;
    private int quantity;
    private double itemPrice;
    private double totalPrice;

    public CheckOutEachResponseDto(CheckOutEach checkOutEach) {
        this.checkOutEachId = checkOutEach.getId();
        this.userId = checkOutEach.getUser().getId();
        this.itemId = checkOutEach.getItem().getId();
        this.itemName = checkOutEach.getItem().getTitle();
        this.quantity = checkOutEach.getQuantity();
        this.itemPrice = checkOutEach.getItem().getPrice();
        this.totalPrice = this.quantity * this.itemPrice;
    }
}
