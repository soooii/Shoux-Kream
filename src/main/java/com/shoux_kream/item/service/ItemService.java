package com.shoux_kream.item.service;

import com.shoux_kream.exception.ErrorCode;
import com.shoux_kream.exception.KreamException;
import com.shoux_kream.item.dto.request.ItemSaveRequest;
import com.shoux_kream.item.dto.request.ItemUpdateRequest;
import com.shoux_kream.item.dto.response.BrandResponse;
import com.shoux_kream.item.dto.response.ItemResponse;
import com.shoux_kream.item.dto.response.ItemUpdateResponse;
import com.shoux_kream.item.entity.Brand;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.repository.BrandRepository;
import com.shoux_kream.item.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;

    public ItemService(ItemRepository productRepository, BrandRepository brandRepository) {
        this.itemRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    @Transactional
    public ItemResponse save(ItemSaveRequest itemSaveRequest) {
        Brand brand = findBrandById(itemSaveRequest.brandId());

        //
        Item item = new Item(brand, itemSaveRequest.name(), itemSaveRequest.itemInfo(), itemSaveRequest.size());
        Item savedItem = itemRepository.save(item);

        return new ItemResponse(
                savedItem.getId(),
                new BrandResponse(savedItem.getBrand().getId(), savedItem.getBrand().getName()),
                savedItem.getName(),
                savedItem.getItemInfo());
    }

    public ItemResponse findById(Long id) {
        Item item = findItemById(id);

        return ItemResponse.fromEntity(item);
    }

    public ItemResponse findByName(String itemName) {
        Item item = itemRepository.findByName(itemName)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        return ItemResponse.fromEntity(item);
    }

    @Transactional
    public ItemUpdateResponse update(ItemUpdateRequest itemUpdateRequest) {
        Brand brand = findBrandById(itemUpdateRequest.brandId());

        itemRepository.updateItemInfo(
                itemUpdateRequest.brandId(),
                itemUpdateRequest.itemName(),
                itemUpdateRequest.color(),
                itemUpdateRequest.modelNumber());

        return new ItemUpdateResponse(
                brand.getName(),
                itemUpdateRequest.itemName(),
                itemUpdateRequest.color(),
                itemUpdateRequest.modelNumber()
        );
    }

    public List<ItemResponse> findAll() {
        return itemRepository.findAll().stream()
                .map(ItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private Brand findBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new KreamException(ErrorCode.INVALID_ID));
    }

    private Item findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new KreamException(ErrorCode.INVALID_ID));
    }
}