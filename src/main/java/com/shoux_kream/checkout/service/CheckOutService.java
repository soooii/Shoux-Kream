package com.shoux_kream.checkout.service;

import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.entity.CheckOut;
import com.shoux_kream.checkout.repository.CheckOutRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckOutService {
    private final CheckOutRepository checkOutRepository;

    // List타입 회원 주문조회 리스트 반환(다중조회)
    @Transactional
    public List<CheckOutResponseDto> getCheckOuts(Long id) {
        List<CheckOutResponseDto> checkOutResponseDtos = new ArrayList<>();
        return checkOutResponseDtos;
    }

    // 단건 정보 조회
    @Transactional
    public CheckOutResponseDto getCheckOut(Long id) {
        CheckOut checkOut = checkOutRepository.getReferenceById(id);
        CheckOutResponseDto checkOutResponseDto = CheckOutResponseDto.builder()
                .id(checkOut.getId())
                .carts(checkOut.getCarts())
                .receipt(checkOut.getReceipt())
                .build();
        return checkOutResponseDto;
    }

    public CheckOutResponseDto save(CheckOutRequestDto checkOutRequestDto) {
        // 저장한 뒤 id값이 포함된 값을 반환
        CheckOutResponseDto checkOutResponseDto = checkOutRepository.save(checkOutRequestDto.toEntity());

        return checkOutResponseDto;
    }
}
