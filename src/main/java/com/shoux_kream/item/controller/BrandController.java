//package com.shoux_kream.item.controller;
//
//import com.shoux_kream.item.dto.request.BrandSaveRequest;
//import com.shoux_kream.item.dto.response.BrandResponse;
//import com.shoux_kream.item.dto.response.BrandsGetResponse;
//import com.shoux_kream.item.entity.Brand;
//import com.shoux_kream.item.service.BrandService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/brand")
//public class BrandController {
//
//    private final BrandService brandService;
//
//    public BrandController(BrandService brandService) {
//        this.brandService = brandService;
//    }
//
////    @GetMapping
////    public List<Brand> getAllBrands() {
////        return brandService.findAllBrands();
////    }
//
//    // 모든 인증된 사용자 접근 가능 - 주어진 id에 해당하는 브랜드 정보를 조회하고 반환
//    @GetMapping("/{brandId}")
//    @ResponseBody  // JSON 응답을 원할 때 추가
//    public String getBrandInfo(@PathVariable Long brandId) {
//        BrandResponse findBrandById = brandService.findById(brandId);
//        return "";
//    }
//
//    // 모든 인증된 사용자 접근 가능 - 모든 브랜드의 목록을 조회하여 응답
//    @GetMapping
//    @ResponseBody  // JSON 응답을 원할 때 추가
//    public String getBrandsInfo() {
//        List<BrandResponse> brandList = brandService.findAll();
//        return "";
//    }
//
//    // 관리자 권한 필요 - 새로운 브랜드를 등록하고, 등록된 브랜드 정보를 반환
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    @ResponseBody  // JSON 응답을 원할 때 추가
//    public ResponseEntity<BrandResponse> saveBrand(@RequestBody BrandSaveRequest brandSaveRequest) {
//        BrandResponse savedBrandResponse = brandService.save(brandSaveRequest.name());
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(savedBrandResponse);
//    }
//
//    // 관리자 권한 필요 - 주어진 id에 해당하는 브랜드를 삭제
//    @PreAuthorize("hasRole('ADMIN')")
//    @DeleteMapping("/{brandId}")
//    public ResponseEntity<Void> deleteBrand(@PathVariable Long brandId) {
//        brandService.delete(brandId);
//        return ResponseEntity.noContent().build();
//    }
//}