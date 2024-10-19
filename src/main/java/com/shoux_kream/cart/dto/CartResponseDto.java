package com.shoux_kream.cart.dto;

import com.shoux_kream.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CartResponseDto {

    private Long cartId;
    private Long userId;
    private Long itemId;

    private LocalDateTime createdAt;
    private int quantity;

    private String itemName;
    private String imageUrl;
    private double itemPrice;

    private double totalPrice;

    public CartResponseDto(Cart cart) {
        this.cartId = cart.getId();
        this.userId = cart.getUser().getId();
        this.itemId = cart.getItem().getId();
        this.createdAt = cart.getCreatedAt();
        this.quantity = cart.getQuantity();
        this.itemName = cart.getItem().getTitle();
        this.imageUrl = cart.getItem().getImageKey();
        this.itemPrice = cart.getItem().getPrice();
        this.totalPrice = this.quantity * this.itemPrice;
    }

}
