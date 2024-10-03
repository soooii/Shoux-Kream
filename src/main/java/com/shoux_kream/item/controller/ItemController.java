package com.shoux_kream.item.controller;

import com.shoux_kream.item.dto.request.ItemSaveRequest;
import com.shoux_kream.item.dto.request.ItemUpdateRequest;
import com.shoux_kream.item.dto.response.ItemResponse;
import com.shoux_kream.item.dto.response.ItemUpdateResponse;
import com.shoux_kream.item.dto.response.ItemsGetResponse;
import com.shoux_kream.item.service.ItemService;
import com.shoux_kream.item.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 모든 인증된 사용자가 접근 가능 (유저 및 관리자) - 모든 상품의 목록을 조회하여 응답
    @GetMapping
    public ResponseEntity<ItemsGetResponse> getItems() {
        List<ItemResponse> itemList = itemService.findAll();

        return ResponseEntity.ok(new ItemsGetResponse(itemList.size(), itemList));
    }

    // 관리자 권한 필요 - 새로운 상품을 등록하고, 등록된 상품 정보를 응답으로 반환
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ItemResponse> saveItem(@RequestBody ItemSaveRequest itemSaveRequest) {
        ItemResponse savedItemResponse = itemService.save(itemSaveRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedItemResponse);
    }

    // 관리자 권한 필요 - 기존 상품 정보를 수정하고, 수정된 정보를 응답으로 반환
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<ItemUpdateResponse> updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest) {
        ItemUpdateResponse itemUpdateResponse = itemService.update(itemUpdateRequest);

        return ResponseEntity.ok().body(itemUpdateResponse);
    }

    // 관리자 권한 필요 - 주어진 id에 해당하는 상품을 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.delete(itemId);
        return ResponseEntity.noContent().build();
    }
}