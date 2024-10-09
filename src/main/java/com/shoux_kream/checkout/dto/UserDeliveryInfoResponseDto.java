package com.shoux_kream.checkout.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeliveryInfoResponseDto {
    private String phoneNumber;
    private String postalCode;
    private String address1;
    private String address2;

    // Method to convert Entity to Response DTO
//    public static UserDeliveryInfoResponseDto fromEntity(UserDeliveryInfo userDeliveryInfo) {
//        return UserDeliveryInfoResponseDto.builder()
//                .phoneNumber(userDeliveryInfo.getPhoneNumber())
//                .postalCode(userDeliveryInfo.getPostalCode())
//                .address1(userDeliveryInfo.getAddress1())
//                .address2(userDeliveryInfo.getAddress2())
//                .build();
//    }
}
