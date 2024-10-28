package com.shoux_kream.user.entity;

import com.shoux_kream.user.dto.response.UserAddressDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 수령인 이름
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    // 수령인 전화번호
    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    // 우편번호
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    //주소
    @Column(name = "address1", nullable = false)
    private String address1;

    //상세주소
    @Column(name = "address2")
    private String address2;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 주소 정보 수정
    public UserAddress update(UserAddressDto userAddressDto) {
        this.recipientName = userAddressDto.getRecipientName();
        this.recipientPhone = userAddressDto.getRecipientPhone();
        this.postalCode = userAddressDto.getPostalCode();
        this.address1 = userAddressDto.getAddress1();
        this.address2 = userAddressDto.getAddress2();
        return this;
    }
}

