package com.shoux_kream.cart.service;

import com.shoux_kream.cart.dto.CartRequestDto;
import com.shoux_kream.cart.dto.CartResponseDto;
import com.shoux_kream.cart.entity.Cart;
import com.shoux_kream.cart.repository.CartRepository;
import com.shoux_kream.exception.CartQuantityOutOfRangeException;
import com.shoux_kream.item.entity.Item;
import com.shoux_kream.item.repository.ItemRepository;
import com.shoux_kream.user.entity.Role;
import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // User 객체 초기화
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .nickname("TestNick")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(Role.USER) // Role.USER는 enum으로 정의된 것에 맞게 설정
                .build();

        // Item 객체 초기화
        item = new Item("Test Item", "Test Manufacturer", "Short Description",
                "Detailed Description", "imageKey", 100, 1000, Collections.singletonList("test, item"));
    }

    // 장바구니 상품 추가
    @DisplayName("장바구니 상품 추가 - 이미 존재하는 상품")
    @Test
    void addCart_itemAlreadyInCartTest() {
        Cart existingCart = new Cart(user, item, 1); // 상품을 미리 추가

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(cartRepository.findByItemAndUser(item, user)).thenReturn(Optional.of(existingCart));

        CartRequestDto cartRequestDto = new CartRequestDto(1L, 2);
        Long cartId = cartService.addCart(cartRequestDto, 1L); // if문이 실행되어 이미 상품이 존재하므로 수량 증가

        assertEquals(existingCart.getId(), cartId);
        assertEquals(3, existingCart.getQuantity());
        verify(cartRepository).save(existingCart);
    }

    @DisplayName("장바구니 상품 추가 - 새로 상품 추가")
    @Test
    void addCart_newItemTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(cartRepository.findByItemAndUser(item, user)).thenReturn(Optional.empty());

        CartRequestDto cartRequestDto = new CartRequestDto(1L, 2);
        Long cartId = cartService.addCart(cartRequestDto, 1L);

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository).save(cartCaptor.capture());

        Cart savedCart = cartCaptor.getValue();
        assertNotNull(savedCart);
        assertEquals(user, savedCart.getUser());
        assertEquals(item, savedCart.getItem());
        assertEquals(2, savedCart.getQuantity());
        assertEquals(cartId, savedCart.getId());
    }

    // 장바구니 조회
    @DisplayName("장바구니 조회")
    @Test
    void allCartsTest() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(List.of(new Cart(user, item, 1)));

        List<CartResponseDto> carts = cartService.allCarts(1L);

        assertNotNull(carts);
        assertEquals(1, carts.size());
    }

    // 장바구니 수정
    @DisplayName("장바구니 수정")
    @Test
    void updateCartTest() {
        Long cartId = 1L;
        CartRequestDto cartRequestDto = new CartRequestDto(user.getId(), 5);
        Cart cart = new Cart(user, item, 1);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        CartResponseDto updateCartResponseDto = cartService.updateCart(cartRequestDto, cartId);

        assertNotNull(updateCartResponseDto);
        assertEquals(cart.getId(), updateCartResponseDto.getCartId());
        assertEquals(cartRequestDto.getUserId(), updateCartResponseDto.getUserId());
        assertEquals(cartRequestDto.getQuantity(), updateCartResponseDto.getQuantity());
    }

    @DisplayName("장바구니 수정 - 수량이 0개 이하일 때")
    @Test
    void updateCartOutOfRangeTest() {
        Long cartId = 1L;
        CartRequestDto cartRequestDto = new CartRequestDto(user.getId(), 0);
        Cart cart = new Cart(user, item, 1);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        assertThrows(CartQuantityOutOfRangeException.class, () -> cartService.updateCart(cartRequestDto, cartId));
    }

    // 장바구니 삭제
    @DisplayName("장바구니 선택 삭제")
    @Test
    void deleteCartsTest() {
        List<Long> cartIds = Arrays.asList(1L, 2L, 3L);

        cartRepository.deleteCartsSelected(user.getId(), cartIds);

        verify(cartRepository).deleteCartsSelected(user.getId(), cartIds);

    }

    @DisplayName("장바구니 개별 삭제")
    @Test
    void deleteCartTest() {
        Long cartId = 1L;

        cartRepository.deleteById(cartId);

        verify(cartRepository).deleteById(cartId);
    }
    
  
}