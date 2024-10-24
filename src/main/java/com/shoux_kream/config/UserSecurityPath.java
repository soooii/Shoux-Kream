package com.shoux_kream.config;

public class UserSecurityPath {
    public static final String[] ONLY_GET_WHITELIST = {
            // => 그냥 기본 view 반환 경로(ViewController)
            "/api/cart/selected",
            "/api/cart/summary",
            "/api/checkout",
            "/api/checkout/{detailId}",
            "/api/sale/item/{itemId}",
            "/api/sale",
            "/api/users/**",
            "/api/checkout-item/{checkoutId}"
    };
    public static final String[] ONLY_POST_WHITELIST = {
            "/api/cart/add/{itemId}",
            "/api/checkout",
            "/api/checkout-item",
            "/api/sale/sell",
            "/api/sale/{saleId}",
            "/api/users/**"
            
    };
    public static final String[] ONLY_PATCH_WHITELIST = {
            "/api/cart/selected",
            "/api/cart/edit/{cartId}",
            "/api/checkout/{detailId}",
            "/api/sale/{saleId}",
            "/api/users/**"

            
    };
    public static final String[] ONLY_DELETE_WHITELIST = {
            "/api/cart/",
            "/api/cart/{cartId}",
            "/api/checkout/{detailId}",
            "/api/sale/{saleId}",
            "/api/users/**"
            
    };
}
