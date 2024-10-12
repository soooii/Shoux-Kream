package com.shoux_kream.checkout.dto;

import com.shoux_kream.checkout.entity.CheckOutItem;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
public class CheckOutItemRequestDto {
    private Long checkOutId;
    private Long itemId; // Assuming you have a user entity
    private int quantity; // List of items to checkout
    private int totalPrice; // Payment method (e.g., credit card, PayPal)
}