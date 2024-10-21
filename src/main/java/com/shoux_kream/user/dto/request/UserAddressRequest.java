package com.shoux_kream.user.dto.request;

import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.entity.UserAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAddressRequest {
    private Long id;
    private String recipientName;
    private String recipientPhone;
    private String postalCode;
    private String address1;
    private String address2;

    // UserAddress 엔티티에서 DTO를 생성하는 빌더 생성자
    @Builder
    public UserAddressRequest(UserAddress userAddress) {
        this.id = userAddress.getId();
        this.recipientName = userAddress.getRecipientName();
        this.recipientPhone = userAddress.getRecipientPhone();
        this.postalCode = userAddress.getPostalCode();
        this.address1 = userAddress.getAddress1();
        this.address2 = userAddress.getAddress2();
    }

    // User 객체를 매개변수로 받아 UserAddress 엔티티로 변환하는 메서드
    public UserAddress toEntity(User user) {
        return UserAddress.builder()
                .recipientName(recipientName)
                .recipientPhone(recipientPhone)
                .postalCode(postalCode)
                .address1(address1)
                .address2(address2)
                .user(user) // User 객체를 설정
                .build();
    }
}
