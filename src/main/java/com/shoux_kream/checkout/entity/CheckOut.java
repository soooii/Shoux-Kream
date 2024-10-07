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

    private String summaryTitle;
    private int totalPrice;
    private String address;
    //배송요청사항
    private String request;

    @OneToMany
    @JoinColumn(name = "cart_id")
    List<Cart> carts;

    @OneToOne
    @JoinColumn(name = "receipt_id")
    Receipt receipt;

    @Builder
    public CheckOut(Long id, String summaryTitle, int totalPrice, String address, String request, List<Cart> carts, Receipt receipt) {
        Id = id;
        this.summaryTitle = summaryTitle;
        this.totalPrice = totalPrice;
        this.address = address;
        this.request = request;
        this.carts = carts;
        this.receipt = receipt;
    }
}
