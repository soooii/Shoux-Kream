package com.shoux_kream.config;

public class SecurityPath {
    public static final String[] ONLY_GET_WHITELIST = {
            "/users/login",
            "/users/signup",
            "/",
            "/users/me"
    };
    public static final String[] ONLY_POST_WHITELIST = {
            "/api/users/login",
            "/api/users/signup",
            "/api/users/refresh"
    };
}
