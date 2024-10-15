package com.shoux_kream.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoux_kream.checkout.entity.DeliveryStatus;
import com.shoux_kream.user.dto.response.UserAddressDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckOutResponseDto {
    // - 주문번호 id
    //- summaryTitle(주문 요약 제목)
    //- totalPrice(총 가격)
    //- deliveryStatus(배송상태) → 수정시 셀렉트박스로 수정하면됌
    //- UserId(유저id) (class)
    //- address(주소지) (class) address 1 , 2
    //- request(배송요청사항)
    private Long id;
    private String summaryTitle;
    private int totalPrice;
    private DeliveryStatus deliveryStatus;
    private Long userId;
    private UserAddressDto address;
    private String request;

    @Builder
    public CheckOutResponseDto(Long id,
                               String summaryTitle,
                               int totalPrice,
                               DeliveryStatus deliveryStatus,
                               Long userId,
                               UserAddressDto address,
                               String request) {
        this.id = id;
        this.summaryTitle = summaryTitle;
        this.totalPrice = totalPrice;
        this.deliveryStatus = deliveryStatus;
        this.userId = userId;
        this.address = address;
        this.request = request;
    }
}
