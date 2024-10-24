package com.shoux_kream.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserViewController {

    @GetMapping("/login")
    public String login() {return "user/login";}

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }

    @GetMapping("/me")
    public String getMyPage() {return "user/mypage-main";}

    @GetMapping("/me/account")
    public String getAccountPage() {return "user/mypage-account";}

    @GetMapping("/me/purchase")
    public String getPurchasePage() {return "user/mypage-purchase";}

    @GetMapping("/me/purchase/{checkoutId}")
    public String getCheckOutDetailPage() {return "user/mypage-checkout-detail";}

    @GetMapping("/me/address")
    public String getAddressPage() {return "user/mypage-address";}


}
