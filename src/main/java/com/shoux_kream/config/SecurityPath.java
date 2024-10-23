package com.shoux_kream.config;

public class SecurityPath {
    public static final String[] ONLY_GET_WHITELIST = {
            "/users/login",
            "/users/signup",
            "/",
            "/users/me",
            "/users/me/account",
            "/users/me/address",
            "/item/item-list",
            "/item/item-detail/{id}",
            "/item/edit/{id}",
            "/item/item-add",
            "/category/category-add",
            "/category/add",
            "/category/edit/{id}"
    };
    public static final String[] ONLY_POST_WHITELIST = {
            "/api/users/login",
            "/api/users/signup",
            "/api/users/refresh",
            "/item/item-add",
            "/category/category-add",
            "/category/add",
            "/category/edit/{id}"
    };
}
