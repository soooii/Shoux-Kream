package com.shoux_kream.checkout.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Table(name = "receipt")
@Entity
@Getter
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
}
