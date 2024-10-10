//package com.shoux_kream.item.service;
//
//import com.shoux_kream.exception.ErrorCode;
//import com.shoux_kream.exception.KreamException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.shoux_kream.item.dto.response.BrandResponse;
//import com.shoux_kream.item.entity.Brand;
//import com.shoux_kream.item.repository.BrandRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.shoux_kream.exception.ErrorCode.INVALID_ID;
//
//@Service
//@Transactional(readOnly = true)
//public class BrandService {
//
//    private final BrandRepository brandRepository;
//
//    public BrandService(BrandRepository brandRepository) {
//        this.brandRepository = brandRepository;
//    }
//
//    public List<Brand> findAllBrands() {
//        return brandRepository.findAll();
//    }
//
//    // 새로운 브랜드를 등록하고 저장된 브랜드 정보를 반환
//    @Transactional
//    public BrandResponse save(String name) {
//        Brand brand = new Brand(name);
//        Brand savedBrand = brandRepository.save(brand);
//        return new BrandResponse(savedBrand.getId(), savedBrand.getTitle());
//    }
//
//    // 주어진 id에 해당하는 브랜드를 삭제 (존재하지 않으면 예외 발생)
//    @Transactional
//    public void delete(Long id) {
//        if (!brandRepository.existsById(id)) {
//            throw new KreamException(INVALID_ID);
//        }
//        brandRepository.deleteById(id);
//    }
//
//    // 모든 브랜드의 목록을 조회하고 dto 리스트로 반환
//    public List<BrandResponse> findAll() {
//        return brandRepository.findAll().stream()
//                .map(BrandResponse::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    // 주어진 이름에 해당하는 브랜드를 조회하고 dto 로 변환하여 반환
//    public BrandResponse findByName(String name) {
//        Brand brand = findBrandByName(name);
//        return BrandResponse.fromEntity(brand);
//    }
//
//    // 주어진 id에 해당하는 브랜드를 조회하고 dto 로 변환하여 반환
//    public BrandResponse findById(Long id) {
//        Brand brand = findBrandById(id);
//        return BrandResponse.fromEntity(brand);
//    }
//
//    // 주어진 id에 해당하는 브랜드를 내부적으로 조회 (없으면 예외 발생)
//    private Brand findBrandById(Long id) {
//        if (id == null) {
//            throw new KreamException(ErrorCode.INVALID_ID);
//        }
//        return brandRepository.findById(id)
//                .orElseThrow(() -> new KreamException(ErrorCode.INVALID_ID));
//    }
//
//
//    // 주어진 이름에 해당하는 브랜드를 내부적으로 조회 (없으면 예외 발생)
//    private Brand findBrandByName(String title) {
//        return brandRepository.findByTitle(title)
//                .orElseThrow(() -> new KreamException(INVALID_ID));
//    }
//}