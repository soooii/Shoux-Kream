package com.shoux_kream.config;

public class SecurityPath {
    public static final String[] ONLY_GET_WHITELIST = {
            "/users/login",
            "/users/signup",
            "/",
            "/users/me",
            "/item/item-list",
            "/item/item-detail/{id}",
            "/item/edit/{id}",
            "/item/item-add"
    };
    public static final String[] ONLY_POST_WHITELIST = {
            "/api/users/login",
            "/api/users/signup",
            "/api/users/refresh",
            "/item/item-add"
    };
}
