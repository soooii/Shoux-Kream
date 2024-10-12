package com.shoux_kream.checkout.service;

//import com.shoux_kream.checkout.CheckOutMapper;
import com.shoux_kream.checkout.dto.CheckOutItemRequestDto;
import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.entity.CheckOutItem;
import com.shoux_kream.checkout.repository.AddressRepository;
import com.shoux_kream.checkout.repository.CheckOutItemRepository;
import com.shoux_kream.checkout.repository.CheckOutRepository;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.repository.ItemRepository;
import com.shoux_kream.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final AddressRepository addressRepository;

    public CheckOutResponseDto createCheckout(Long userId, CheckOutRequestDto checkOutRequestDto) {
        CheckOut checkOut = CheckOut.builder()
                .summaryTitle(checkOutRequestDto.getSummaryTitle())
                .totalPrice(checkOutRequestDto.getTotalPrice())
                .address(checkOutRequestDto.getAddress().toEntity())
                .request(checkOutRequestDto.getRequest())
                .build();
        checkOutRepository.save(checkOut);
        return checkOut.toDto();
    }

    public List<CheckOut> getCheckoutsByUserId(Long userId) {
        return checkOutRepository.findByUserId(userId);
    }

    public void addCheckOutItem(CheckOutItemRequestDto checkoutItemRequestDto) {
        CheckOut checkOut = checkOutRepository.findById(checkoutItemRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid checkout ID"));

        CheckOutItem checkOutItem = CheckOutItem.builder()
                .item(checkoutItemRequestDto.getCheckOutItem().getItem())
                .quantity(checkoutItemRequestDto.getCheckOutItem().getQuantity())
                .totalPrice(checkoutItemRequestDto.getCheckOutItem().getTotalPrice())
                .build();

        checkOutItemRepository.save(checkOutItem);
    }

    public List<CheckOut> getCheckOuts(Long userId) {
        return checkOutRepository.findByUserId(userId);
    }

    // More methods for updating, deleting checkout and checkout items
}
