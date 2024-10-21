package com.shoux_kream.item.controller;

import com.shoux_kream.item.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @DeleteMapping("/item-detail/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}