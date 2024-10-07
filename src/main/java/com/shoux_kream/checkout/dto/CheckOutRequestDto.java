package com.shoux_kream.checkout.dto;

import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.entity.Receipt;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class CheckOutRequestDto {

    public CheckOut toEntity(Receipt receipt, List<Cart> carts){
        return CheckOut.builder().receipt(receipt).carts(carts).build();
    }
}
