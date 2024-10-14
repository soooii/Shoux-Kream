package com.shoux_kream.config.jwt;

import com.shoux_kream.user.entity.Role;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface JwtProvider <T> {
    T convertAuthToken(String token);
    Authentication getAuthentication(T authToken);
    T createAccessToken(String sub, Role role, Map<String, Object> claims);
    T createRefreshToken(String sub, Role role, Map<String, Object> claims);
}

