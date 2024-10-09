package com.shoux_kream.checkout.entity;


import com.shoux_kream.user.entity.User;
import jakarta.persistence.*;

import java.util.List;

//TODO user쪽 이동 필요
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postalCode;
    private String address1;
    private String address2;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "address")
    private List<CheckOut> checkOuts;

    // Getters and setters
}