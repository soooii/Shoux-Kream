package com.shoux_kream.checkout.controller;

import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.service.CheckOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckOutApiController {
    private final CheckOutService checkOutService;
    @GetMapping
    public ResponseEntity<CheckOutResponseDto> getCheckOut(@RequestBody CheckOutRequestDto checkOutRequestDto){
        Model model = null;
        // user id 를 얻어서 카트를 얻음

        //혹은 url에서 id값 박아넣어서
        return ResponseEntity.ok();
    }
}
