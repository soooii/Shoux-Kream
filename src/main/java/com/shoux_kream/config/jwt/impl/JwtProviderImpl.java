package com.shoux_kream.config.jwt.impl;

import com.shoux_kream.config.jwt.JwtProvider;
import com.shoux_kream.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.*;

import static com.shoux_kream.config.jwt.AuthToken.AUTHORITIES_TOKEN_KEY;
import static com.shoux_kream.config.jwt.UserConstants.ACCESS_TOKEN_TYPE_VALUE;
import static com.shoux_kream.config.jwt.UserConstants.REFRESH_TOKEN_TYPE_VALUE;

@Component
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider<AuthTokenImpl> {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.access-expires}")
    private long accessExpires;

    @Value("${jwt.token.refresh-expires}")
    private long refreshExpires;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key= Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public AuthTokenImpl convertAuthToken(String token) {
        return new AuthTokenImpl(token, key);
    }

    @Override
    public Authentication getAuthentication(AuthTokenImpl authToken) {
        if(authToken.validate()){
            Claims claims = authToken.getDate().getClaims();
            if(!claims.get("type").equals(ACCESS_TOKEN_TYPE_VALUE)){
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid token type"
                );
            }

            Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                    new SimpleGrantedAuthority(claims.get(
                            AUTHORITIES_TOKEN_KEY,
                            String.class)
                    ));

            User principal =
                    new User(claims.getSubject(), "", authorities); // 사용자 정보 객체 생성

            return new UsernamePasswordAuthenticationToken(principal,authToken,authorities);}
        else {
            throw new JwtException("token Error");
        }
    }

    @Override
    public AuthTokenImpl createAccessToken(String sub, Role role, Map<String, Object> claims) {
        claims.put("jti",UUID.randomUUID().toString());
        claims.put("type", ACCESS_TOKEN_TYPE_VALUE);
        return new AuthTokenImpl(
                sub,
                role,
                key,
                new DefaultClaims(claims),
                new Date(System.currentTimeMillis() + accessExpires)
        );
    }




    @Override
    public AuthTokenImpl createRefreshToken(String sub, Role role, Map<String, Object> claims) {
        claims.put("jti",UUID.randomUUID().toString());
        claims.put("type", REFRESH_TOKEN_TYPE_VALUE);
        return new AuthTokenImpl(
                sub,
                role,
                key,
                new DefaultClaims(claims),
                new Date(System.currentTimeMillis() + refreshExpires)
        );
    }
}