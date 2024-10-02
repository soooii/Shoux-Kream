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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<ItemsGetResponse> getItems() {
        List<ItemResponse> itemList = itemService.findAll();

        return ResponseEntity.ok(new ItemsGetResponse(itemList.size(), itemList));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> saveItem(@RequestBody ItemSaveRequest itemSaveRequest) {
        ItemResponse savedItemResponse = itemService.save(itemSaveRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedItemResponse);
    }

    @PatchMapping
    public ResponseEntity<ItemUpdateResponse> updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest) {
        ItemUpdateResponse itemUpdateResponse = itemService.update(itemUpdateRequest);

        return ResponseEntity.ok().body(itemUpdateResponse);
    }
}
