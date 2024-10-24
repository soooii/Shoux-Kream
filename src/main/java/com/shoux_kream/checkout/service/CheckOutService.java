package com.shoux_kream.checkout.service;

import com.shoux_kream.checkout.dto.*;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.entity.CheckOutEach;
import com.shoux_kream.checkout.entity.CheckOutItem;
import com.shoux_kream.checkout.entity.DeliveryStatus;
import com.shoux_kream.checkout.repository.CheckOutEachRepository;
import com.shoux_kream.checkout.repository.CheckOutItemRepository;
import com.shoux_kream.checkout.repository.CheckOutRepository;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.repository.ItemRepository;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


//TODO orelseThrow globalexception 공통처리
@Service
@RequiredArgsConstructor
public class CheckOutService {
    //TODO Mapper 이용 리팩토링 고려
//    private final CheckOutMapper mapper = CheckOutMapper.INSTANCE;
    private final CheckOutRepository checkOutRepository;
    private final CheckOutEachRepository checkOutEachRepository;
    private final CheckOutItemRepository checkOutItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<CheckOutItemResponseDto> getCheckOutItem(Long checkoutId){
        List<CheckOutItem> checkOuts = checkOutItemRepository.findByCheckOutId(checkoutId);
        return checkOuts.stream()
                .map(checkOutItem -> checkOutItem.toDto())
                .collect(Collectors.toList());
    }

    //TODO Transactional을 언제, 왜, 어떻게 써야할까요?
    @Transactional
    public CheckOutResponseDto createCheckout(Long userId, CheckOutRequestDto checkOutRequestDto) {
        CheckOut checkOut = CheckOut.builder()
                .summaryTitle(checkOutRequestDto.getSummaryTitle())
                .totalPrice(checkOutRequestDto.getTotalPrice())
                .user(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("userId not found")))
                .address(checkOutRequestDto.getAddress().toEntity())
                .request(checkOutRequestDto.getRequest())
                .build();
        checkOutRepository.save(checkOut);
        return checkOut.toDto();
    }

    @Transactional
    public void addCheckOutItem(CheckOutItemRequestDto checkoutItemRequestDto) {
        CheckOutItem checkOutItem = CheckOutItem.builder()
                .item(itemRepository.findById(checkoutItemRequestDto.getItemId()).orElseThrow(() -> new IllegalArgumentException("itemId not found")))
                .quantity(checkoutItemRequestDto.getQuantity())
                .totalPrice(checkoutItemRequestDto.getTotalPrice())
                .checkOut(checkOutRepository.findById(checkoutItemRequestDto.getCheckOutId()).orElseThrow(() -> new IllegalArgumentException("checkoutId not found")))
                .build();
        checkOutItemRepository.save(checkOutItem);

        // 주문 완료 후 재고 감소
        Item item = itemRepository.findById(checkoutItemRequestDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("itemId not found"));
        item.removeStock(checkoutItemRequestDto.getQuantity());
    }

    public List<CheckOutResponseDto> getCheckOuts(Long userId) {
        List<CheckOut> checkOuts = checkOutRepository.findByUserId(userId);
        return checkOuts.stream()
                .map(checkOut -> checkOut.toDto())
                .collect(Collectors.toList());
    }

    public CheckOutResponseDto updateCheckOut(Long detailId, CheckOutRequestDto checkOutRequestDto) {
        CheckOut checkOut =  checkOutRepository.findById(detailId).orElseThrow(() -> new IllegalArgumentException("checkoutId not found"));
        checkOut.updateAddress(checkOutRequestDto);
        checkOutRepository.save(checkOut);
        return  checkOut.toDto();
    }

    public CheckOutResponseDto getCheckOutDetail(Long detailId) {
        CheckOut checkOut = checkOutRepository.findById(detailId).orElseThrow(() -> new IllegalArgumentException("checkoutId not found"));
        return checkOut.toDto();
    }

    public Long deleteCheckOut(Long detailId) {
        checkOutRepository.deleteById(detailId);
        return detailId;
    }

    public CheckOutResponseDto updateDeliveryStatus(Long detailId, DeliveryStatus deliveryStatus) {
        CheckOut checkOut = checkOutRepository.findById(detailId).orElseThrow(() -> new IllegalArgumentException("checkoutId not found"));
        checkOut.updateDeliveryStatus(deliveryStatus);
        return checkOut.toDto();
    }

    // More methods for updating, deleting checkout and checkout items

    // 즉시 구매 조회
    public CheckOutEachResponseDto getCheckOutEach(Long userId) {
        CheckOutEach checkOutEach = checkOutEachRepository.findByUserId(userId);

        return new CheckOutEachResponseDto(checkOutEach);
    }

    // 즉시 구매를 위해 정보 임시 저장
    @Transactional
    public Long addCheckOutEach(Long userId, CheckOutEachRequestDto checkOutEachRequestDto) {
        checkOutEachRepository.deleteByUserId(userId); // 사용자가 뒤로 갔다가 올 경우를 대비한 DB 초기화

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Item item = itemRepository.findById(checkOutEachRequestDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("Item not found. item id : " + checkOutEachRequestDto.getItemId()));

        CheckOutEach checkOutEach = new CheckOutEach(user, item, checkOutEachRequestDto.getQuantity());
        checkOutEachRepository.save(checkOutEach);
        return checkOutEach.getId();
    }

    // 주문완료 후 즉시 구매 삭제
    @Transactional
    public void deleteCheckOutEach(Long checkOutEacId) {
        checkOutEachRepository.deleteById(checkOutEacId);
    }

    // 어드민 주문 내역 전체 조회
    public List<CheckOutResponseDto> getAllCheckOuts() {
        List<CheckOut> checkOuts = checkOutRepository.findAll();
        return checkOuts.stream()
                .map(checkOut -> checkOut.toAdminDto())
                .collect(Collectors.toList());
    }

    public Long deleteUserCheckOut(Long detailId) {
        CheckOut checkOut = checkOutRepository.findById(detailId).orElseThrow(() -> new IllegalArgumentException("checkoutId not found"));
        checkOutRepository.delete(checkOut);
        return detailId;
    }
}