package com.shoux_kream.checkout.service;

import com.shoux_kream.checkout.dto.CheckOutItemRequestDto;
import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.entity.CheckOutItem;
import com.shoux_kream.checkout.entity.DeliveryStatus;
import com.shoux_kream.checkout.repository.CheckOutItemRepository;
import com.shoux_kream.checkout.repository.CheckOutRepository;
import com.shoux_kream.item.repository.ItemRepository;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


//TODO orelseThrow globalexception 공통처리
@Service
@RequiredArgsConstructor
public class CheckOutService {
    //TODO Mapper 이용 리팩토링 고려
//    private final CheckOutMapper mapper = CheckOutMapper.INSTANCE;
    private final CheckOutRepository checkOutRepository;
    private final CheckOutItemRepository checkOutItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

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
    }

    public List<CheckOutResponseDto> getCheckOuts(Long userId) {
        List<CheckOut> checkOuts = checkOutRepository.findByUserId(userId);
        return checkOuts.stream()
                .map(checkOut -> checkOut.toDto())
                .collect(Collectors.toList());
    }

    public CheckOutResponseDto updateCheckOut(Long detailId, UserAddressDto userAddressDto) {
        CheckOut checkOut =  checkOutRepository.findById(detailId).orElseThrow(() -> new IllegalArgumentException("checkoutId not found"));
        checkOut.updateAddress(userAddressDto.toEntity());
        checkOutRepository.save(checkOut);
        return  checkOut.toDto();
    }

    public CheckOutResponseDto getCheckOutDetail(Long detailId) {
        CheckOut checkOut = checkOutRepository.findById(detailId).orElseThrow(() -> new IllegalArgumentException("checkoutId not found"));
        return checkOut.toDto();
    }

    public Long deleteCheckOut(String email, Long detailId) {
        CheckOut checkOut = checkOutRepository.findByUserAndId(userRepository.findByEmail(email).orElseThrow(), detailId);
        checkOutRepository.delete(checkOut);
        return detailId;
    }

    public CheckOutResponseDto updateDeliveryStatus(Long detailId, DeliveryStatus deliveryStatus) {
        CheckOut checkOut = checkOutRepository.findById(detailId).orElseThrow(() -> new IllegalArgumentException("checkoutId not found"));
        checkOut.updateDeliveryStatus(deliveryStatus);
        return checkOut.toDto();
    }

    // More methods for updating, deleting checkout and checkout items
}