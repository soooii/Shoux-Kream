package com.shoux_kream.user.service;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.shoux_kream.config.jwt.impl.AuthTokenImpl;
import com.shoux_kream.config.jwt.impl.JwtProviderImpl;
import com.shoux_kream.user.dto.JwtTokenDto;
import com.shoux_kream.user.dto.request.JwtTokenLoginRequest;
import com.shoux_kream.user.dto.request.UserRequest;
import com.shoux_kream.user.dto.response.UserAddressDto;
import com.shoux_kream.user.dto.response.UserResponse;
import com.shoux_kream.user.entity.RefreshToken;
import com.shoux_kream.user.entity.Role;
import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.entity.UserAddress;
import com.shoux_kream.user.repository.RefreshTokenRepository;
import com.shoux_kream.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProviderImpl jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    // private final RefreshTokenService refreshTokenService;

    @jakarta.annotation.PostConstruct
    public void init() {
        User user = User.builder()
                .email("1@1")
                .password(bCryptPasswordEncoder.encode("1"))
                .name("elice")
                .nickname("e")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    //회원가입
    public Long signup(UserRequest dto) {
        return userRepository.save(User.builder()
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(Role.USER)
                .build()).getId();
    }

    //회원 조회
    public UserResponse getUser(String email) {
        Optional<User> user = userRepository.findByEmail(email); //optional 예외처리 필요
        return new UserResponse(user.get().getId(), user.get().getEmail());
    }

    //회원정보 수정
    public UserResponse updateProfile(String email, UserRequest dto) {
        log.info(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        User updatedUser = User.builder()
                .id(user.getId())
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .role(user.getRole())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build();

        userRepository.save(updatedUser);
        return new UserResponse(updatedUser.getId(), updatedUser.getEmail());
    }

    //회원정보 삭제
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }

    //로그인
    public JwtTokenDto login(JwtTokenLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("accountId", user.getId());
        claims.put("role", user.getRole());

        String sub = request.getEmail();
        AuthTokenImpl accessToken = jwtProvider.createAccessToken(
                sub,
                user.getRole(),
                claims
        );

        AuthTokenImpl refreshToken = jwtProvider.createRefreshToken(
                sub,
                user.getRole(),
                claims
        );

        //Refresh Token 레디스에 저장
        // refreshTokenService.save(sub, refreshToken.getToken());
        RefreshToken token = RefreshToken.builder().refreshToken(refreshToken.getToken()).email(request.getEmail()).build();
        refreshTokenRepository.save(token);

        return JwtTokenDto.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public List<UserAddressDto> getUserAddresses(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));
        //optional 예외처리 적용

        List<UserAddress> userAddresses = user.getAddresses();

        return userAddresses.stream()
                .map(UserAddress -> new UserAddressDto(UserAddress))
                .collect(Collectors.toList());
    }
}


