package com.shoux_kream.item.controller;

import com.shoux_kream.item.dto.request.ItemSaveRequest;
import com.shoux_kream.item.dto.request.ItemUpdateRequest;
import com.shoux_kream.item.dto.response.ItemResponse;
import com.shoux_kream.item.dto.response.ItemUpdateResponse;
import com.shoux_kream.item.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/item")
public class ItemApiController {

    private final ItemService itemService;

    public ItemApiController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 조회용 GET 메서드 추가
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable("id") Long id) {
        ItemResponse itemResponse = itemService.findItemById(id);
        return ResponseEntity.ok(itemResponse);
    }

    // 관리자 권한 필요 - 새로운 상품을 등록하고, 등록된 상품 정보를 응답으로 반환
    @PostMapping("/item-add")
    public ResponseEntity<ItemResponse> saveItem(@ModelAttribute ItemSaveRequest itemSaveRequest,
                                                 @RequestParam("imageKey") MultipartFile imageFile) throws IOException {
        ItemResponse savedItemResponse = itemService.save(itemSaveRequest, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedItemResponse);
    }
    // 관리자 권한 필요 - 수정용 PUT 메서드
    @PatchMapping("/{id}")
    public ResponseEntity<ItemUpdateResponse> updateItemById(@PathVariable("id") Long id,
                                                             @RequestParam(value = "imageKey", required = false) MultipartFile imageFile,
                                                             @ModelAttribute ItemUpdateRequest itemUpdateRequest) throws Exception {
        ItemUpdateResponse itemUpdateResponse = itemService.update(id, itemUpdateRequest, imageFile);
        return ResponseEntity.ok(itemUpdateResponse);
    }
    // 관리자 권한 필요 -
    @DeleteMapping("/item-detail/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }


}