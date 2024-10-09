package com.shoux_kream.checkout.entity;

import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.timestamp.BaseEntity;
import com.shoux_kream.user.entity.User;
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
    private Long id;

    private String summaryTitle;
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private String request;

    //이름 ? mappedBy 이름 문제인가?
    @OneToMany(mappedBy = "check_out", cascade = CascadeType.ALL)
    private List<CheckOutItem> checkOutItems;

    @Builder
    public CheckOut(List<CheckOutItem> checkOutItems, String request, Address address, User user, int totalPrice, String summaryTitle, Long id) {
        this.checkOutItems = checkOutItems;
        this.request = request;
        this.address = address;
        this.user = user;
        this.totalPrice = totalPrice;
        this.summaryTitle = summaryTitle;
        this.id = id;
    }
}
