package com.shoux_kream.checkout.entity;

import com.shoux_kream.item.entity.Item;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class CheckOutItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //TODO 테이블 이름이 items!
    @JoinColumn(name = "items_id")
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
}
