package com.shoux_kream.item.service;
import com.shoux_kream.cart.repository.CartRepository;
import com.shoux_kream.category.entity.Category;
import com.shoux_kream.category.repository.CategoryRepository;
import com.shoux_kream.config.s3.S3Uploader;
import com.shoux_kream.exception.ErrorCode;
import com.shoux_kream.exception.KreamException;
import com.shoux_kream.item.dto.request.ItemSaveRequest;
import com.shoux_kream.item.dto.request.ItemUpdateRequest;
import com.shoux_kream.item.dto.response.ItemResponse;
import com.shoux_kream.item.dto.response.ItemUpdateResponse;
//import com.shoux_kream.item.entity.Brand;
import com.shoux_kream.item.dto.response.SaleItemResponseDto;
import com.shoux_kream.item.entity.Item;
//import com.shoux_kream.item.repository.BrandRepository;
import com.shoux_kream.item.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
//    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final S3Uploader s3Uploader;
    private final CartRepository cartRepository;

    // 새로운 상품을 등록하고 저장된 상품 정보를 반환
    @Transactional
    public ItemResponse save(ItemSaveRequest itemSaveRequest,  MultipartFile imageFile) throws IOException {

//        Brand brand = findBrandById(itemSaveRequest.brandId());
        Category category = findCategoryById(itemSaveRequest.categoryId());
        //TODO 카테고리 미지정 선택불가 처리

        String imageKey = s3Uploader.upload(imageFile, "item-images");

        Item item = new Item(
//              brand,
                itemSaveRequest.id(),
                itemSaveRequest.title(),
                category, // Category 엔티티 사용
                itemSaveRequest.manufacturer(),
                itemSaveRequest.shortDescription(),
                itemSaveRequest.detailDescription(),
                imageKey,
                itemSaveRequest.inventory(),
                itemSaveRequest.price(),
                itemSaveRequest.keyWords()
        );

        Item savedItem = itemRepository.save(item);

        return new ItemResponse(
                savedItem.getId(),
//                new BrandResponse(savedItem.getBrand().getId(), savedItem.getBrand().getTitle()),
                savedItem.getTitle(),
                savedItem.getCategory(), // Category 엔티티 사용
                savedItem.getManufacturer(),
                savedItem.getShortDescription(),
                savedItem.getDetailDescription(),
                savedItem.getImageKey(),
                savedItem.getInventory(),
                savedItem.getPrice(),
                savedItem.getKeyWords()
        );
    }

    public ItemResponse findItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return ItemResponse.fromEntity(item); // 조회용 DTO 반환
    }

    // 주어진 id에 해당하는 상품을 조회하고 dto 로 변환하여 반환
    public ItemResponse findById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 상품을 찾을 수 없습니다."));
        return ItemResponse.fromEntity(item); // Entity를 ItemResponse로 변환 후 반환
    }

    // 주어진 상품 이름에 해당하는 상품을 조회하고 dto 로 변환하여 반환
    public ItemResponse findByName(String itemTitle) {
        Item item = itemRepository.findByTitle(itemTitle)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
        return ItemResponse.fromEntity(item);
    }



    // 기존 상품 정보를 수정하고, 수정된 정보를 반환
    @Transactional
    public ItemUpdateResponse update(Long id, ItemUpdateRequest itemUpdateRequest, MultipartFile imageFile) throws Exception {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageKey = s3Uploader.upload(imageFile, "item-images");
            item.setImageKey(imageKey);  // 새로운 이미지 키로 업데이트
        }
        Category category = findCategoryById(itemUpdateRequest.categoryId());

        // Item의 필드 값 업데이트
        item.setTitle(itemUpdateRequest.title());
        item.setCategory(category);
        item.setManufacturer(itemUpdateRequest.manufacturer());
        item.setShortDescription(itemUpdateRequest.shortDescription());
        item.setDetailDescription(itemUpdateRequest.detailDescription());
        item.setInventory(itemUpdateRequest.inventory());
        item.setPrice(itemUpdateRequest.price());
        item.setKeyWords(itemUpdateRequest.keyWords());

        // 업데이트된 내용을 저장
        itemRepository.save(item);

        return new ItemUpdateResponse(
                item.getId(),
                item.getTitle(),
                item.getCategory(),
                item.getManufacturer(),
                item.getShortDescription(),
                item.getDetailDescription(),
                imageFile, // MultipartFile을 그대로 넘김
                item.getInventory(),
                item.getPrice(),
                item.getKeyWords()
        );
    }

    public ItemUpdateRequest getUpdateRequestById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return new ItemUpdateRequest(
                item.getId(),
                item.getTitle(),
                item.getCategory().getId(),
                item.getManufacturer(),
                item.getShortDescription(),
                item.getDetailDescription(),
                item.getInventory(),
                item.getPrice(),
                item.getKeyWords()
        );
    }

    // 주어진 id에 해당하는 상품을 삭제 (존재하지 않으면 예외 발생)
    //TODO 삭제 비활성화
    @Transactional
    public void delete(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        // S3에서 이미지 삭제 코드 (선택 사항)
//        s3Uploader.deleteImage(item.getImageKey());
        // Cart에서 해당 Item과 관련된 항목 삭제
        cartRepository.deleteByItemId(itemId);

        itemRepository.delete(item); // 데이터베이스에서 상품 삭제
    }

    // 모든 상품의 목록을 조회하고 dto 리스트로 반환
    // TODO 추가
    public List<ItemResponse> findAll() {
        return itemRepository.findAll().stream()
                .map(ItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 주어진 id에 해당하는 카테고리를 내부적으로 조회 (없으면 예외 발생)
    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new KreamException(ErrorCode.INVALID_ID));
    }

    // 판매 등록을 위한 상품 찾기
    public SaleItemResponseDto findSaleItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found. item id : " + itemId));

        return new SaleItemResponseDto(item);
    }

    // 상품 검색
    public List<ItemResponse> searchItems(String searchKeyword) {
        return itemRepository.findByTitleContaining(searchKeyword).stream()
                .map(ItemResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
