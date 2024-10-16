package com.shoux_kream.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoux_kream.MockSecurityConfig;
import com.shoux_kream.cart.dto.CartRequestDto;
import com.shoux_kream.cart.service.CartService;
import com.shoux_kream.config.jwt.impl.JwtProviderImpl;
import com.shoux_kream.exception.JwtAuthenticationEntryPoint;
import com.shoux_kream.user.dto.response.UserResponse;
import com.shoux_kream.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MockSecurityConfig.class) // MockSecurityConfig를 임포트하여 사용
@WebMvcTest(CartApiController.class)
class CartApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtProviderImpl jwtProvider;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 추가


    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse userResponse;

    private String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiYWNjb3VudElkIjo1LCJyb2xlIjoiVVNFUiIsInR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJleHAiOjE3Mjg5Nzc0MTh9._6_Fq1aKd3pfNgec90tJDXN6AgIyIQ3UnDU4AthFDos";

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse(1L, "test@example.com", "Test User");
    }

    @DisplayName("장바구니 추가 - 비회원의 경우")
    @Test
    @WithMockUser(username = "test@example.com")
    void addCartTestForbidden() throws Exception {
        // given
        Long itemId = 1L;
        CartRequestDto cartRequestDto = new CartRequestDto();
        cartRequestDto.setUserId(userResponse.getUserId());
        cartRequestDto.setQuantity(2);

        // when
        // getUser 인자값 필요없어짐
        when(userService.getUser()).thenReturn(userResponse);
        when(cartService.addCart(any(CartRequestDto.class), any(Long.class))).thenReturn(1L);

        // then
        mockMvc.perform(post("/api/cart/add/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequestDto)))
                .andExpect(status().isForbidden());
    }

}