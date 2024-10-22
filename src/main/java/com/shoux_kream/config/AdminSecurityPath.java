package com.shoux_kream.config;

public class AdminSecurityPath {
    public static final String[] ONLY_GET_WHITELIST = {
            // => 그냥 기본 view 반환 경로(ViewController)
            "/api/admim/**",
            "/api/users/admin-check",
            "/category/{id}"
    };
    public static final String[] ONLY_POST_WHITELIST = {
            "/api/admin/**",
            "/category/category-add",
            "/item/item-add"
    };
    public static final String[] ONLY_PATCH_WHITELIST = {
            "/api/admin/**",
            "/category/{id}",
            "/item/{id}"
    };
    public static final String[] ONLY_DELETE_WHITELIST = {
            "/api/admin/**",
            "/category/{id}",
            "/item-detail/{id}"
    };
}
