package com.shoux_kream.checkout.entity;

import com.shoux_kream.item.entity.Item;
import com.shoux_kream.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Table(name = "check_out_each")
public class CheckOutEach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "items_id", nullable = false)
    private Item item;

    public CheckOutEach(int quantity, Item item) {
        this.quantity = quantity;
        this.item = item;
    }
}
