package com.shoux_kream.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "blacklist_tokens")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jti;

    private Date expirationTime;

    public BlacklistToken(String jti, Date expirationTime) {
        this.jti = jti;
        this.expirationTime = expirationTime;
    }
}