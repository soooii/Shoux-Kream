package com.shoux_kream.item.dto.response;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ItemUpdateResponse(
//        String brandName,
        Long id,
//        BrandResponse brand,
        String title,
//        Category category,
        String manufacturer,
        String shortDescription,
        String detailDescription,
        MultipartFile image,
        Integer inventory,
        Integer price,
        List<String> keyWords // 배열로 받기 위해 List<String> 사용
) {
}
