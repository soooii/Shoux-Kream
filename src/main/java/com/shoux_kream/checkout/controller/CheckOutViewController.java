package com.shoux_kream.checkout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CheckOutViewController {
    /*
    상품id

상품명

상품가격

상품 수량

상품 옵션

상품 썸네일

상품 정보

---------------------------- productDTO

주문자 id

주문자 이름

주문자 주소

주문자 전화번호

---------------------------- memberVO

주문번호

주문 일렬번호(여러개의 상품을 동시에 주문할 때, 주문번호 하나를 동일하게 가지는 개별상품들을 구분하는 번호)

결제시간

상품배송상태

배송메세지

총 결제금액

---------------------------- 추가가 필요한 내용

=================================================== orderDTO
[출처] [Spring] 쇼핑몰 만들기 11. 주문하기 기능 구현 (1) 개별상품 주문하기|작성자 워니
     */

    // 단 건 체크아웃
    @GetMapping("/checkout-each")
    public String getCheckOutEachPage() { return "checkout/checkout-each"; }

    // 카트의 정보를 받아서 모델에 넣어서 프론트에서 처리해야한다. model에 cart정보 넣기
    @GetMapping("/checkout")
    public String getCheckOutPage(){
        return "checkout/checkout";
    }

    @GetMapping("/checkout/complete")
    public String getCheckOutCompletePage(){
        return "checkout/checkoutcomplete";
    }
}
