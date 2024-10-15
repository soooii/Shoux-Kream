package com.shoux_kream.checkout.entity;


// https://kadosholy.tistory.com/114
public enum DeliveryStatus {
    READY("발송준비"),
    START("배송시작"),
    SHIPPING("배송중"),
    ARRIVED("배송예정");
    private final String status;


    DeliveryStatus(String status) {
        this.status = status;
    }

    String getStatus(){
        return status;
    }
}
