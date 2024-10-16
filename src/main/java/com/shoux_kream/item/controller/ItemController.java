package com.shoux_kream.item.controller;

import com.shoux_kream.config.s3.S3Uploader;
import com.shoux_kream.item.dto.ItemDto;
import com.shoux_kream.item.dto.request.ItemSaveRequest;
import com.shoux_kream.item.dto.request.ItemUpdateRequest;
import com.shoux_kream.item.dto.response.ItemResponse;
import com.shoux_kream.item.dto.response.ItemUpdateResponse;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final S3Uploader s3Uploader;

    public ItemController(ItemService itemService, S3Uploader s3Uploader) {
        this.itemService = itemService;
        this.s3Uploader = s3Uploader;
    }

    // 모든 인증된 사용자가 접근 가능 (유저 및 관리자) - 모든 상품의 목록을 조회하여 응답
    @GetMapping("/item-list")
    public String getItems(Model model) {
        List<ItemResponse> items = itemService.findAll();
        model.addAttribute("items", items);
        return "item/item-list";
    }

    // 상품을 클릭했을때 나오는 상세 페이지(ex.발매가, 사이즈 선택)
    @GetMapping("/item-detail/{id}")
    public String getItemPage(@PathVariable Long id, Model model) {
        ItemResponse item = itemService.findById(id); // 특정 ID의 상품을 조회
        model.addAttribute("item", item);
        return "item/item-detail";
    }

    // 관리자 권한 필요 - 새로운 상품을 등록하고, 등록된 상품 정보를 응답으로 반환
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/item-add")
    public ResponseEntity<ItemResponse> saveItem(@ModelAttribute ItemSaveRequest itemSaveRequest,
                                                 @RequestParam("imageKey") MultipartFile imageFile) throws IOException {
        ItemResponse savedItemResponse = itemService.save(itemSaveRequest, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedItemResponse);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/item-add") // GET 요청으로 상품 등록 페이지를 불러오는 메서드
    public String getItemAddPage(){
        return "item/item-add";
    }

    // 조회용 GET 메서드 추가
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        ItemResponse itemResponse = itemService.findItemById(id);
        return ResponseEntity.ok(itemResponse);
    }

    // 수정용 PUT 메서드
//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ItemUpdateResponse> updateItemById(@PathVariable Long id,
                                                             @RequestParam(value = "imageKey", required = false) MultipartFile imageFile,
                                                             @ModelAttribute ItemUpdateRequest itemUpdateRequest) throws Exception {
        ItemUpdateResponse itemUpdateResponse = itemService.update(id, itemUpdateRequest, imageFile);
        return ResponseEntity.ok(itemUpdateResponse);
    }


    // 상품 수정 페이지 뷰
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditItemPage(@PathVariable Long id, Model model) {
        model.addAttribute("itemId", id);
        return "item/item-edit";
    }
}