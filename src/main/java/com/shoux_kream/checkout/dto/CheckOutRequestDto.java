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
public class CheckOutRequestDto {
    private String summaryTitle;
    private int totalPrice;
    private String address;
    //배송요청사항
    private String request;

    List<Cart> carts;
    Receipt receipt;
    @Builder
    public CheckOutRequestDto(String summaryTitle, int totalPrice, String address, String request, List<Cart> carts, Receipt receipt) {
        this.summaryTitle = summaryTitle;
        this.totalPrice = totalPrice;
        this.address = address;
        this.request = request;
        this.carts = carts;
        this.receipt = receipt;
    }


    public CheckOut toEntity(){
        return CheckOut.builder()
                .summaryTitle(summaryTitle)
                .totalPrice(totalPrice)
                .address(address)
                .request(request)
                .build();
    }
}
