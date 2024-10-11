package com.shoux_kream.config.jwt;

import com.shoux_kream.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//토큰 유효성 검사 결과
@Getter
@RequiredArgsConstructor
public class JwtTokenValidationResult {
    private final boolean valid;
    private final ErrorCode errorCode;
    private final Claims claims;

    public static JwtTokenValidationResult valid(Claims claims) {
        return new JwtTokenValidationResult(true, null, claims);
    }

    public static JwtTokenValidationResult invalid(ErrorCode errorCode) {
        return new JwtTokenValidationResult(false,errorCode,null);
    }
}
