package com.shoux_kream.cart.dto;

import com.shoux_kream.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartRequestDto {
    private Long userId;
    private int quantity;

    public CartRequestDto(Cart cart) {
        this.userId = cart.getUser().getId();
        this.quantity = cart.getQuantity();
    }

}
