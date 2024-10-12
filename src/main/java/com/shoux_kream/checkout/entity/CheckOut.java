package com.shoux_kream.checkout.entity;

import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.timestamp.BaseEntity;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.entity.UserAddress;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Table(name="check_out")
@Entity
@Getter
public class CheckOut extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String summaryTitle;
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private UserAddress address;

    private String request;

    //TODO 이름 ? mappedBy 이름 문제인가? => 필드 이름 매핑! 테이블이름 X
    @OneToMany(mappedBy = "checkOut", cascade = CascadeType.ALL)
    private List<CheckOutItem> checkOutItems;

    @Builder
    public CheckOut(List<CheckOutItem> checkOutItems, String request, UserAddress address, User user, int totalPrice, String summaryTitle) {
        this.checkOutItems = checkOutItems;
        this.request = request;
        this.address = address;
        this.user = user;
        this.totalPrice = totalPrice;
        this.summaryTitle = summaryTitle;
    }

    public CheckOutResponseDto toDto() {
        return CheckOutResponseDto.builder()
                .id(this.id)
                .summaryTitle(this.summaryTitle)
                .totalPrice(this.totalPrice)
                .address(UserAddressDto.builder().userAddress(this.address).build())
                .request(this.request)
                .build();
    }
}
