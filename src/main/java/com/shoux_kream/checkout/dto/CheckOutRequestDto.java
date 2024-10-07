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

    private Long Id;
    List<Cart> carts;
    Receipt receipt;

    @Builder
    public CheckOutRequestDto(Long id, List<Cart> carts, Receipt receipt) {
        Id = id;
        this.carts = carts;
        this.receipt = receipt;
    }

    public CheckOut toEntity(Receipt receipt, List<Cart> carts){
        return CheckOut.builder().receipt(receipt).carts(carts).build();
    }
}
