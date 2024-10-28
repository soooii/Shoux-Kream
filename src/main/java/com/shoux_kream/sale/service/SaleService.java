package com.shoux_kream.sale.service;

import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.repository.ItemRepository;
import com.shoux_kream.sale.dto.SaleRequestDto;
import com.shoux_kream.sale.dto.SaleResponseDto;
import com.shoux_kream.sale.entity.Sale;
import com.shoux_kream.sale.repository.SaleRepository;
import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // 판매 등록
    @Transactional
    public Long sellItem(Long userId, SaleRequestDto saleRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Item item = itemRepository.findById(saleRequestDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("Item not found. item id : " + saleRequestDto.getItemId()));

        Sale newSale = new Sale(saleRequestDto.getSellingPrice(),
                saleRequestDto.getDaysToAdd(),
                user, item);

        saleRepository.save(newSale);

        // 판매 등록 후 재고 증가
        int quantity = 1;
        item.increaseStock(quantity);
        return newSale.getId();
    }
    
    // 판매 내역 조회
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSales(Long userId) {
        List<Sale> sales = saleRepository.findByUserId(userId);

        return sales.stream()
                .map(SaleResponseDto::new)
                .collect(Collectors.toList());
    }

    // 판매 등록 후 완료 페이지
    @Transactional(readOnly = true)
    public SaleResponseDto getSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId).orElse(null);

        return new SaleResponseDto(sale);
    }

    // 판매 등록 후 수정
    @Transactional
    public SaleResponseDto updateSale(Long saleId, SaleRequestDto saleRequestDto) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new NoSuchElementException("SaleItem not found"));

        sale.updateDaysToAdd(saleRequestDto.getDaysToAdd());
        sale.updateSellingPrice(saleRequestDto.getSellingPrice());

        return new SaleResponseDto(sale);
    }

    // 사용자 판매 상품 삭제
    public void deleteSale(Long saleId) {
        saleRepository.deleteById(saleId);
    }

}
