package com.shoux_kream.user.controller;

import com.shoux_kream.config.jwt.impl.AuthTokenImpl;
import com.shoux_kream.config.jwt.impl.JwtProviderImpl;
import com.shoux_kream.user.dto.JwtTokenDto;
import com.shoux_kream.user.dto.request.JwtTokenLoginRequest;
import com.shoux_kream.user.dto.response.JwtTokenResponse;
import com.shoux_kream.user.dto.response.UserResponse;
import com.shoux_kream.user.entity.Role;
import com.shoux_kream.user.repository.RefreshTokenRepository;
import com.shoux_kream.user.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class JwtController {
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProviderImpl tokenProvider;

    //로그인
    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> jwtLogin(
            @RequestBody JwtTokenLoginRequest request,
            HttpServletResponse response
    ) {
        JwtTokenDto jwtTokenResponse = userService.login(request);

        //Refresh Token 쿠키에 저장
        Cookie refreshCookie = new Cookie("refreshToken", jwtTokenResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true); //HTTPS에서만 전송되도록
        refreshCookie.setSecure(true); //쿠키 유효 경로
        refreshCookie.setPath("/"); //자바스크립트에서 접근할 수 없게
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); //쿠키 유효 기간
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().body(JwtTokenResponse
                .builder()
                .accessToken(jwtTokenResponse.getAccessToken())
                .build()
        );
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal User principal, HttpServletResponse response) {
        String email = principal.getUsername();

        Cookie refreshTokenCookie = new Cookie("refreshToken", null); // 쿠키 값을 null로 설정
        refreshTokenCookie.setHttpOnly(true); // HttpOnly 속성 설정
        refreshTokenCookie.setMaxAge(0); // 쿠키 유효 기간을 0으로 설정
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가

        refreshTokenRepository.deleteByEmail(email);

        return ResponseEntity.ok().build();
    }

    // Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenResponse> refresh(HttpServletRequest request) {
        Optional<String> token = resolveRefreshToken(request);

        if (token.isPresent()) {
            AuthTokenImpl jwtToken = tokenProvider.convertAuthToken(token.get());
            if (jwtToken.validate()) {
                Claims claims = jwtToken.getDate();
                String email = claims.getSubject();
                Role role = jwtToken.getRole();
                if(refreshTokenRepository.findByEmail(email).isPresent()) {
                    if(refreshTokenRepository.findByEmail(email).get().getRefreshToken().equals(token.get())) {
                        String newAccessToken = tokenProvider.createAccessToken(email, role, claims).getToken();
                        JwtTokenResponse dto = new JwtTokenResponse(newAccessToken);
                        return ResponseEntity.ok(dto);
                    }
                }
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 401 Unauthorized
    }


    // 쿠키의 Refresh Token 가져옴
    private Optional<String> resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }


}
