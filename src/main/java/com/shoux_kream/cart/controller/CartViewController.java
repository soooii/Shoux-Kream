package com.shoux_kream.cart.controller;

import com.shoux_kream.cart.dto.CartResponseDto;
import com.shoux_kream.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartViewController {

    private final CartService cartService;

    @GetMapping("/summary/{userId}")
    public String getCarts(@PathVariable("userId") Long userId, Model model) {

        List<CartResponseDto> carts = cartService.allCarts(userId);

        model.addAttribute("carts", carts); // 장바구니 내용
        model.addAttribute("userId", userId); // 카트 버튼 클릭 시 다시 카트 내용으로 이동

        return "cart/cart";
    }
}
