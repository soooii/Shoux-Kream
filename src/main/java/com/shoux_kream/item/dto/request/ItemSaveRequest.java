package com.shoux_kream.item.dto.request;

import com.shoux_kream.item.entity.Item;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ItemSaveRequest(
        Long brandId,
        String title,
//        Long categoryId, // Category의 ID만 받도록 수정
        String manufacturer,
        String shortDescription,
        String detailDescription,
        String imageKey,
        int inventory,
        int price,
        List<String> searchKeywords, // 배열로 받기 위해 List<String> 사용
        Long itemInfoId, // ItemInfo의 ID만 받도록 수정
        int size
) {
}