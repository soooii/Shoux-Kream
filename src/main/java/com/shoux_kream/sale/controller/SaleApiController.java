package com.shoux_kream.sale.controller;

import com.shoux_kream.item.dto.response.SaleItemResponseDto;
import com.shoux_kream.item.service.ItemService;
import com.shoux_kream.sale.dto.SaleRequestDto;
import com.shoux_kream.sale.dto.SaleResponseDto;
import com.shoux_kream.sale.service.SaleService;
import com.shoux_kream.user.dto.response.UserResponse;
import com.shoux_kream.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale")
@RequiredArgsConstructor
public class SaleApiController {

    private final SaleService saleService;
    private final UserService userService;
    private final ItemService itemService;

    // 사용자 판매 상품 등록
    @PostMapping("/sell")
    public ResponseEntity sellItem(@RequestBody SaleRequestDto saleRequestDto) {
        UserResponse userResponse = userService.getUser();

        Long saleId = saleService.sellItem(userResponse.getUserId(), saleRequestDto);
        return ResponseEntity.ok(saleId);
    }

    // 사용자 판매 등록 시 아이템 조회
    @GetMapping("/item/{itemId}")
    public ResponseEntity getItemDetail(@PathVariable("itemId") Long itemId) {
        SaleItemResponseDto saleItemResponseDto = itemService.findSaleItemById(itemId);

        return ResponseEntity.ok(saleItemResponseDto);
    }
    
    // 사용자 판매 내역 마이페이지 - 판매 목록에서 보여줌
    @GetMapping
    public ResponseEntity<List<SaleResponseDto>> getSales() {
        UserResponse userResponse = userService.getUser();
        List<SaleResponseDto> sales = saleService.getSales(userResponse.getUserId());

        return ResponseEntity.ok(sales);
    }

    // 판매 등록 완료 후 내역 조회
    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponseDto> getSale(@PathVariable("saleId") Long saleId) {
        SaleResponseDto saleResponseDto = saleService.getSale(saleId);

        return ResponseEntity.ok(saleResponseDto);
    }

    // 사용자 판매 상품 수정
    @PatchMapping("/{saleId}")
    public ResponseEntity updateSale (@PathVariable("saleId") Long saleId, @RequestBody SaleRequestDto saleRequestDto) {
        SaleResponseDto saleResponseDto = saleService.updateSale(saleId, saleRequestDto);

        return ResponseEntity.ok(saleResponseDto);
    }
    
    // 사용자 판매 상품 삭제
    @DeleteMapping("/{saleId}")
    public ResponseEntity deleteSale (@PathVariable("saleId") Long saleId) {
        saleService.deleteSale(saleId);

        return ResponseEntity.ok(saleId);
    }
}
