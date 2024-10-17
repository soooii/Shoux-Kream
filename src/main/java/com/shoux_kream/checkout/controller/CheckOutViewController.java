package com.shoux_kream.checkout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout")
public class CheckOutViewController {
    @GetMapping
    public String getCheckOutPage(){
        return "checkout/checkout";
    }
    @GetMapping("/complete")
    public String getCheckOutCompletePage(){
        return "checkout/checkoutcomplete";
    }
}
