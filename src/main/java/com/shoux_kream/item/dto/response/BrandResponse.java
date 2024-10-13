//package com.shoux_kream.item.dto.response;
//
//
//import com.shoux_kream.item.entity.Brand;
//import com.shoux_kream.item.entity.Item;
//import com.shoux_kream.item.entity.ItemInfo;
//
//public record BrandResponse(
//        Long id,
//        String name
//) {
//    public static BrandResponse fromEntity(Brand brand) {
//        return new BrandResponse(brand.getId(), brand.getTitle());
//    }
//}