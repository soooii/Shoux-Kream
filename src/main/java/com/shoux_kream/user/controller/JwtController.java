package com.shoux_kream.user.controller;

import com.shoux_kream.config.jwt.JwtTokenResolver;
import com.shoux_kream.config.jwt.impl.AuthTokenImpl;
import com.shoux_kream.config.jwt.impl.JwtProviderImpl;
import com.shoux_kream.user.dto.JwtTokenDto;
import com.shoux_kream.user.dto.request.JwtTokenLoginRequest;
import com.shoux_kream.user.dto.response.JwtTokenResponse;
import com.shoux_kream.user.entity.BlacklistToken;
import com.shoux_kream.user.entity.RefreshToken;
import com.shoux_kream.user.entity.Role;
import com.shoux_kream.user.repository.JwtBlacklistRepository;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class JwtController {
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final JwtProviderImpl tokenProvider;
    private final JwtTokenResolver tokenResolver;

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

    //로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> accessToken = tokenResolver.resolveToken(request);
        Optional<String> cookieToken = resolveRefreshToken(request);

        if (accessToken.isPresent()) {
            AuthTokenImpl jwtTokenAccess = tokenProvider.convertAuthToken(accessToken.get().split(" ")[1]);
            if (jwtTokenAccess.validate()) {
                String jtiAccess = jwtTokenAccess.getDate().getId();
                Date expired = jwtTokenAccess.getDate().getExpiration();
                BlacklistToken blacklistToken = new BlacklistToken(jtiAccess, expired);
                jwtBlacklistRepository.save(blacklistToken);
            }
        }

        if (cookieToken.isPresent()) {
            String refreshToken = cookieToken.get();
            AuthTokenImpl jwtTokenRefresh = tokenProvider.convertAuthToken(refreshToken);
            if (jwtTokenRefresh.validate()) {
                String jtiRefresh = jwtTokenRefresh.getDate().getId();
                // jti를 통해 해당 리프레시 토큰만 삭제
                refreshTokenRepository.deleteByJti(jtiRefresh);
            }
        }

        // 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(0); // 쿠키 유효 기간을 0으로 설정하여 삭제
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().build();
    }


    // Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenResponse> refresh(HttpServletRequest request) {
        Optional<String> cookieToken = resolveRefreshToken(request);

        if (cookieToken.isPresent()) {
            AuthTokenImpl jwtToken = tokenProvider.convertAuthToken(cookieToken.get());
            if (jwtToken.validate()) {
                Claims claims = jwtToken.getDate();
                String email = claims.getSubject();
                String jti = claims.getId();
                Role role = jwtToken.getRole();

                Optional<RefreshToken> dbToken = refreshTokenRepository.findByJti(jti);
                if (dbToken.isPresent()) {
                    RefreshToken storedRefreshToken = dbToken.get();
                    // db에 저장된 RefreshToken과 쿠키의 RefreshToken이 같은지 검증
                    if (storedRefreshToken.getRefreshToken().equals(cookieToken.get())) {
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
