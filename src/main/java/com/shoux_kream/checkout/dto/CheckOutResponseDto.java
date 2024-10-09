package com.shoux_kream.checkout.dto;

import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.entity.Receipt;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CheckOutResponseDto {

    private String summaryTitle;
    private int totalPrice;
    private UserDeliveryInfoResponseDto address;
    private String request;

    // Method to convert Entity to Response DTO
//    public static CheckoutResponseDto fromEntity(Checkout checkout) {
//        return CheckoutResponseDto.builder()
//                .summaryTitle(checkout.getSummaryTitle())
//                .totalPrice(checkout.getTotalPrice())
//                .address(UserDeliveryInfoResponseDto.fromEntity(checkout.getAddress()))
//                .request(checkout.getRequest())
//                .build();
//    }
}
