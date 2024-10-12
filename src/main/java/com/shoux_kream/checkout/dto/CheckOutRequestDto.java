package com.shoux_kream.checkout.dto;

import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.user.dto.response.UserAddressDto;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
//@Builder
public class CheckOutRequestDto {
    private String summaryTitle;
    private int totalPrice;
    private UserAddressDto address;
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
