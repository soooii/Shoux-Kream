package com.shoux_kream.config.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

//JWT 관련 상수 관리 클래스
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class  UserConstants {
    public static final String AUTHORIZATION_TOKEN_KEY = "Authorization";
    public static final String ACCESS_TOKEN_TYPE_VALUE = "access_token";
    public static final String REFRESH_TOKEN_TYPE_VALUE = "refresh_token";
}
