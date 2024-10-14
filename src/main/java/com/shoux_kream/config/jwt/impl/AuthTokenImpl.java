package com.shoux_kream.config.jwt.impl;


import com.shoux_kream.config.jwt.AuthToken;
import com.shoux_kream.user.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
@Slf4j
public class AuthTokenImpl implements AuthToken<Claims> {
    private final String token;
    private final Key key;

    public AuthTokenImpl(
            String sub,
            Role role,
            Key key,
            Claims claims,
            Date expiredDate
    ) {
        this.key = key;
        this.token = createJwtToken(sub, role, claims, expiredDate).get();
    }

    private Optional<String> createJwtToken(String sub, Role role, Map<String, Object> claims, Date expiredDate) {
        DefaultClaims defaultClaims = new DefaultClaims(claims);
        defaultClaims.put(AUTHORITIES_TOKEN_KEY, role);
        return Optional.ofNullable(
                Jwts.builder()
                        .setSubject(sub)
                        .addClaims(claims)
                        .signWith(key, SignatureAlgorithm.HS256)
                        .setExpiration(expiredDate)
                        .compact()
        );
    }

    @Override
    public boolean validate() {
        return getDate() != null;
    }

    @Override
    public Claims getDate() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key.getEncoded())
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid");
        }
        return null;
    }

    public Role getRole() {
        Claims claims = getDate();
        if (claims != null) {
            String roleValue = claims.get(AUTHORITIES_TOKEN_KEY, String.class); // String으로 가져옴
            return Role.valueOf(roleValue);
        }
        return null;
    }
}

