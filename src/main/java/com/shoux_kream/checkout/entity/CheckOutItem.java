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
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity;
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "checkout_id")
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
