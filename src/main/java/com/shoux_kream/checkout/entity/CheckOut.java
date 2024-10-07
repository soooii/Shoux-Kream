package com.shoux_kream.checkout.entity;

import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.timestamp.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Table(name="check_out")
@Entity
@Getter
public class CheckOut extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToMany
    @JoinColumn(name = "cart_id")
    List<Cart> carts;

    @OneToOne
    @JoinColumn(name = "receipt_id")
    Receipt receipt;

    @Builder
    public CheckOut(Receipt receipt, List<Cart> carts) {
        this.receipt = receipt;
        this.carts = carts;
    }
}
