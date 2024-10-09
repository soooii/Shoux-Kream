package com.shoux_kream.checkout.dto;

import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.entity.Receipt;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
//@Builder
public class CheckOutRequestDto {
    private String summaryTitle;
    private int totalPrice;
    private UserDeliveryInfoRequestDto address;
    private String request;

    // Method to convert Request DTO to Entity
    public CheckOut toEntity() {
        return CheckOut.builder()
                .summaryTitle(summaryTitle)
                .totalPrice(totalPrice)
                .address(address.toEntity())
                .request(request)
                .build();
    }

}
