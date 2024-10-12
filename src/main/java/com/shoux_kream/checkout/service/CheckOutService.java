package com.shoux_kream.checkout.service;

//import com.shoux_kream.checkout.CheckOutMapper;
import com.shoux_kream.checkout.dto.CheckOutItemRequestDto;
import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.entity.CheckOutItem;
import com.shoux_kream.checkout.repository.CheckOutItemRepository;
import com.shoux_kream.checkout.repository.CheckOutRepository;
import com.shoux_kream.item.repository.ItemRepository;
import com.shoux_kream.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckOutService {
    //TODO Mapper 이용 리팩토링 고려
//    private final CheckOutMapper mapper = CheckOutMapper.INSTANCE;
    private final CheckOutRepository checkOutRepository;
    private final CheckOutItemRepository checkOutItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

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

    public List<CheckOut> getCheckoutsByUserId(Long userId) {
        return checkOutRepository.findByUserId(userId);
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

    public List<CheckOut> getCheckOuts(Long userId) {
        return checkOutRepository.findByUserId(userId);
    }

    // More methods for updating, deleting checkout and checkout items
}
