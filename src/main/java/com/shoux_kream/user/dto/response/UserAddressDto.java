package com.shoux_kream.user.dto.response;

import com.shoux_kream.user.entity.UserAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAddressDto {
    private Long id;
    private String recipientName;
    private String recipientPhone;
    private String postalCode;
    private String address1;
    private String address2;

    @Builder
    public UserAddressDto(UserAddress userAddress) {
        this.id = userAddress.getId();
        this.recipientName = userAddress.getRecipientName();
        this.recipientPhone = userAddress.getRecipientPhone();
        this.postalCode = userAddress.getPostalCode();
        this.address1 = userAddress.getAddress1();
        this.address2 = userAddress.getAddress2();
    }

    public UserAddress toEntity() {
        return UserAddress.builder()
                .recipientName(recipientName)
                .recipientPhone(recipientPhone)
                .postalCode(postalCode)
                .address1(address1)
                .address2(address2)
                .build();
    }
}
