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

    private Long Id;
    List<Cart> carts;
    Receipt receipt;

    @Builder
    public CheckOutResponseDto(Long id, List<Cart> carts, Receipt receipt) {
        Id = id;
        this.carts = carts;
        this.receipt = receipt;
    }
}
