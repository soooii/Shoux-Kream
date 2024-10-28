package com.shoux_kream.checkout.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckOutItemResponseDto {
    private Long checkOutId;
    private Long itemId; // Assuming you have a user entity
    private int quantity; // List of items to checkout
    private int totalPrice; // Payment method (e.g., credit card, PayPal)

    @Builder
    public CheckOutItemResponseDto(Long checkOutId, Long itemId, int quantity, int totalPrice) {
        this.checkOutId = checkOutId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
