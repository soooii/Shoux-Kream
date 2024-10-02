package com.shoux_kream.item.controller;

import com.shoux_kream.item.dto.request.BrandSaveRequest;
import com.shoux_kream.item.dto.response.BrandResponse;
import com.shoux_kream.item.dto.response.BrandsGetResponse;
import com.shoux_kream.item.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<BrandResponse> getBrandInfo(@PathVariable Long brandId) {
        BrandResponse findBrandById = brandService.findById(brandId);

        return ResponseEntity.ok(findBrandById);
    }

    @GetMapping
    public ResponseEntity<BrandsGetResponse> getBrandsInfo() {
        List<BrandResponse> brandList = brandService.findAll();

        return ResponseEntity.ok(new BrandsGetResponse(brandList.size(), brandList));
    }

    @PostMapping
    public ResponseEntity<BrandResponse> saveBrand(@RequestBody BrandSaveRequest brandSaveRequest) {
        BrandResponse savedBrandResponse = brandService.save(brandSaveRequest.name());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedBrandResponse);
    }
}