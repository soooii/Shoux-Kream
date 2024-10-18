package com.shoux_kream.item.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "keyword")
@Getter
@Entity
public class KeyWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
