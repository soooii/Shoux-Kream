package com.shoux_kream.cart.dto;

import com.shoux_kream.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CartRequestDto {
    private Long userId;
    private Long itemId;
    private int quantity;

    public CartRequestDto(Cart cart) {
        this.userId = cart.getUser().getId();
        this.itemId = cart.getItem().getId();
        this.quantity = cart.getQuantity();
    }

}
