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
    private Long userId; // Assuming you have a user entity
    private CheckOutItem checkOutItem; // List of items to checkout
    private String paymentMethod; // Payment method (e.g., credit card, PayPal)
}