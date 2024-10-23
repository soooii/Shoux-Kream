package com.shoux_kream.config;

public class SecurityPath {
    public static final String[] ONLY_GET_WHITELIST = {
            "/**.png",
            "/**.jpg",
            "/sale",
            "/users/login",
            "/users/signup",
            "/api/users/logout",
            "/",
            "/users/me",
            "/users/me/account",
            "/users/me/address",
            "/item/item-list",
            "/item/item-detail/{id}",
            "/item/edit/{id}",
            "/item/item-add",
            "/category/{id}",
            "/category/category-add",
            "/category/add",
            "/category/edit/{id}"
            "/cart/summary",
            "/checkout",
            "/checkout-each",
            "/checkout/complete",
            "/sale",
            "/sale/complete"
>>>>>>> src/main/java/com/shoux_kream/config/SecurityPath.java
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
