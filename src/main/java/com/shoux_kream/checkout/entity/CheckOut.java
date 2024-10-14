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
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Table(name="check_out")
@Entity
@Getter
@NoArgsConstructor
public class CheckOut extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String summaryTitle;
    private int totalPrice;


    // TODO https://galid1.tistory.com/572 attribute 컨버터 사용방법도 있음
    @ColumnDefault("발송준비") // 발송준비 배송시작 배송중 배송예정
    @Enumerated(EnumType.STRING) // 스트링 값으로 저장하겠다.
    private DeliveryStatus deliveryStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    //TODO hibernate 오류 해결하기 위한 cascadetype all
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addresses_id")
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

    public void updateAddress(UserAddress address){
        this.address = address;
    }

    public void updateDeliveryStatus(DeliveryStatus deliveryStatus){
        this.deliveryStatus = deliveryStatus;
    }
}
