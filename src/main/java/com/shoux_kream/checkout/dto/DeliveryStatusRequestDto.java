package com.shoux_kream.checkout.dto;

import com.shoux_kream.checkout.entity.DeliveryStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DeliveryStatusRequestDto {
    private DeliveryStatus deliveryStatus;
}
