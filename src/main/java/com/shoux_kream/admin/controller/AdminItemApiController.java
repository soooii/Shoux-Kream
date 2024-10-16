package com.shoux_kream.admin.controller;

import com.shoux_kream.admin.dto.response.UserLogResponse;
import com.shoux_kream.item.dto.response.ItemResponse;
import com.shoux_kream.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/item")
@RequiredArgsConstructor
public class AdminItemApiController {

    private final ItemService itemService;

    @GetMapping()
    public ResponseEntity<List<ItemResponse>> getItems() {
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
