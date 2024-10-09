package com.shoux_kream.checkout.dto;

import com.shoux_kream.checkout.entity.CheckOutItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckOutItemRequestDto {
    private Long userId; // Assuming you have a user entity
    private CheckOutItem checkOutItem; // List of items to checkout
    private String paymentMethod; // Payment method (e.g., credit card, PayPal)
}