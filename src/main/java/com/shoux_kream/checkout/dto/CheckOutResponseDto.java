package com.shoux_kream.checkout.dto;

import com.shoux_kream.user.dto.response.UserAddressDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckOutResponseDto {
    private Long id;
    private String summaryTitle;
    private int totalPrice;
    private UserAddressDto address;
    private String request;

    @Builder
    public CheckOutResponseDto(Long id, String summaryTitle, int totalPrice, UserAddressDto address, String request) {
        this.id = id;
        this.summaryTitle = summaryTitle;
        this.totalPrice = totalPrice;
        this.address = address;
        this.request = request;
    }
}
