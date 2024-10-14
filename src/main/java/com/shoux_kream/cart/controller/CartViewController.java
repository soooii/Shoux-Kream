package com.shoux_kream.cart.controller;

import com.shoux_kream.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartViewController {

    @GetMapping("/summary")
    public String getCarts() {
        return "cart/cart";
    }
}
