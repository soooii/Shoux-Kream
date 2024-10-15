package com.shoux_kream.item.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ItemUpdateRequest(
//        Long brandId,
        Long id,
        String title,
//        Long categoryId, // Category의 ID만 받도록 수정
        String manufacturer,
        String shortDescription,
        String detailDescription,
//        String imageKey,
        MultipartFile image,
        Integer inventory,
        Integer price,
        List<String> searchKeywords // 배열로 받기 위해 List<String> 사용
//        Long itemInfoId, // ItemInfo의 ID만 받도록 수정
//        Integer size
) {
}
