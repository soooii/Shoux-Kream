package com.shoux_kream.checkout.entity;

import com.shoux_kream.item.entity.Item;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
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

    @Builder
    public CheckOutItem(Long id, Item item, int quantity, int totalPrice) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters and setters
}
