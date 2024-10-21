package com.shoux_kream.sale.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class SaleViewController {

    // 판매 입찰하는 과정 -> 판매 등록 후 수정 페이지 함께 사용
    @GetMapping("/sale")
    public String getSale() {
        return "sale/sale";
    }

    // TODO 판매 입찰 완료 페이지
    @GetMapping("/sale/complete")
    public String getSaleComplete() {
        return "sale/salecomplete";
    }

    // TODO 판매 입찰 후 마이페이지에서 판매 내역 확인
    @GetMapping("/selling")
    public String getSelling() {
        return "sale/selling";
    }

    // TODO 마이페이지 판매 내역 -> 상세 내역

}
