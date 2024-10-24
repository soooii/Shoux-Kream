package com.shoux_kream.checkout.entity;

import com.shoux_kream.checkout.dto.CheckOutItemResponseDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.user.dto.response.UserAddressDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CheckOutItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //TODO 테이블 이름이 items!
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity;
    private int totalPrice;

    //TODO table 이름이 check_out이기 때문에 여기에 _id 추가해야함
    @ManyToOne
    @JoinColumn(name = "check_out_id")
    private CheckOut checkOut;

    @Builder
    public CheckOutItem(Item item, int quantity, int totalPrice, CheckOut checkOut) {
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.checkOut = checkOut;
    }

    // Getters and setters

    public CheckOutItemResponseDto toDto() {
        return CheckOutItemResponseDto.builder()
                .itemId(item.getId())
                .quantity(quantity)
                .totalPrice(totalPrice)
                .checkOutId(checkOut.getId())
                .build();
    }
}
