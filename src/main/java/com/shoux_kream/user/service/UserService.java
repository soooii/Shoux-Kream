package com.shoux_kream.user.service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.shoux_kream.config.jwt.impl.AuthTokenImpl;
import com.shoux_kream.config.jwt.impl.JwtProviderImpl;
import com.shoux_kream.user.dto.JwtTokenDto;
import com.shoux_kream.user.dto.request.JwtTokenLoginRequest;
import com.shoux_kream.user.dto.request.UserRequest;
import com.shoux_kream.user.entity.Role;
import com.shoux_kream.user.entity.User;
import com.shoux_kream.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProviderImpl jwtProvider;

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

    public Long save(UserRequest dto) {
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

    public JwtTokenDto login(JwtTokenLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("accountId", user.getId());
        claims.put("role", user.getRole());

        String jti = UUID.randomUUID().toString();
        AuthTokenImpl accessToken = jwtProvider.createAccessToken(
                jti,
                user.getRole(),
                claims
        );

        AuthTokenImpl refreshToken = jwtProvider.createRefreshToken(
                jti,
                user.getRole(),
                claims
        );

        return JwtTokenDto.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
