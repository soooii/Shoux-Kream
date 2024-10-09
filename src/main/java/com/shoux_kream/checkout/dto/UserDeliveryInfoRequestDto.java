package com.shoux_kream.checkout.dto;

import com.shoux_kream.checkout.entity.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeliveryInfoRequestDto {
    private String phoneNumber;
    private String postalCode;
    private String address1;
    private String address2;

    // Method to convert Request DTO to Entity
    //TODO 작동원리 확인 필요
    public Address toEntity() {
        return UserDeliveryInfoRequestDto.builder()
                .phoneNumber(phoneNumber)
                .postalCode(postalCode)
                .address1(address1)
                .address2(address2)
                .build().toEntity();
    }
}
