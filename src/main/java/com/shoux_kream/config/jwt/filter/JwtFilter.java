package com.shoux_kream.config.jwt.filter;

import com.shoux_kream.config.jwt.JwtTokenValidationResult;
import com.shoux_kream.config.jwt.impl.AuthTokenImpl;
import com.shoux_kream.config.jwt.impl.JwtProviderImpl;
import com.shoux_kream.exception.ErrorCode;
import com.shoux_kream.user.entity.RefreshToken;
import com.shoux_kream.user.entity.Role;
import com.shoux_kream.user.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.shoux_kream.config.jwt.UserConstants.AUTHORIZATION_TOKEN_KEY;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProviderImpl tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = resolveToken(request);
        if(token.isPresent()) {
            AuthTokenImpl jwtToken = tokenProvider.convertAuthToken(token.get().split(" ")[1]);
            JwtTokenValidationResult validationResult = jwtToken.getDate();
            if(jwtToken.validate()){
                Authentication authentication = tokenProvider.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else if (validationResult.getErrorCode() == ErrorCode.EXPIRED_TOKEN) {
                handleExpiredToken(request, response, jwtToken);
                return;
            }

        }
        filterChain.doFilter(request, response);

    }

    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, AuthTokenImpl jwtToken) throws IOException {
        Optional<String> refreshToken = resolveRefreshToken(request);

        // Refresh Token이 존재하는지 확인
        if (refreshToken.isPresent()) {
            AuthTokenImpl refreshAuthToken = tokenProvider.convertAuthToken(refreshToken.get());

            // Refresh Token 유효성 검사
            if (refreshAuthToken.validate()) {
                Claims claims = refreshAuthToken.getDate().getClaims();
                String email = claims.getSubject();

                // db에서 Refresh Token 조회
                Optional<RefreshToken> dbToken = refreshTokenRepository.findByEmail(email);

                // db Token 존재 여부와 쿠키의 Refresh Token 비교
                if (dbToken.isPresent() && dbToken.get().getRefreshToken().equals(refreshToken.get())) {

                    // 새로운 Access Token과 Refresh Token 발급
                    String newAccessToken = tokenProvider.createAccessToken(email, claims.get("role",Role.class), new HashMap<>()).getToken();
                    String newRefreshToken = tokenProvider.createRefreshToken(email, claims.get("role", Role.class), new HashMap<>()).getToken();

                    // 새로운 Access Token으로 인증 정보 재설정
                    Authentication authentication = tokenProvider.getAuthentication(tokenProvider.convertAuthToken(newAccessToken));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 새로운 Refresh Token을 db에 저장/업데이트
                    RefreshToken newToken = dbToken.get().updateToken(newRefreshToken);
                    refreshTokenRepository.save(newToken);

                    // 쿠키에 새로운 Refresh Token 저장/업데이트
                    Cookie cookie = new Cookie("refreshToken", newRefreshToken);
                    cookie.setHttpOnly(true); // JavaScript에서 접근 불가능하도록 설정
                    cookie.setPath("/"); // 쿠키가 유효한 경로 설정
                    response.addCookie(cookie); // 응답에 쿠키 추가

                    response.setHeader("Authorization", "Bearer " + newAccessToken);

                } else {
                    // db Refresh Token과 쿠키 Refresh Token이 다르거나 db에 Refresh Token이 존재하지 않음
                    response.sendRedirect("/users/login");
                }
            } else {
                // Refresh Token 유효하지 않음 -> 재로그인
                response.sendRedirect("/users/login");
            }
        } else {
            // Refresh Token 없음 -> 재로그인
            response.sendRedirect("/users/login");
        }
    }

    //AccessToken 가져오기
    private Optional<String> resolveToken(HttpServletRequest request) {
        String authToken = request.getHeader(AUTHORIZATION_TOKEN_KEY);
        if(StringUtils.hasText(authToken)){
            return Optional.of(authToken);
        }else{
            return Optional.empty();
        }
    }

    // 쿠키에서 refreshToken 가져오기
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

    /*
    //Redis 이용 로직
    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, AuthTokenImpl jwtToken) throws IOException {
        Optional<String> refreshToken = resolveRefreshToken(request);

        // Refresh Token이 존재하는지 확인
        if (refreshToken.isPresent()) {
            AuthTokenImpl refreshAuthToken = tokenProvider.convertAuthToken(refreshToken.get());

            // Refresh Token 유효성 검사
            if (refreshAuthToken.validate()) {
                Claims claims = refreshAuthToken.getDate().getClaims();
                String email = claims.getSubject();

                // Redis에서 Refresh Token 조회
                Optional<RefreshToken> redisToken = refreshTokenService.findByToken(email);

                // Redis Token 존재 여부와 쿠키의 Refresh Token 비교
                if (redisToken.isPresent() && redisToken.get().getRefreshToken().equals(refreshToken.get())) {
                    // 새로운 Access Token과 Refresh Token 발급
                    String newAccessToken = tokenProvider.createAccessToken(email, claims.get("role", Role.class), new HashMap<>()).getToken();
                    String newRefreshToken = tokenProvider.createRefreshToken(email, claims.get("role", Role.class), new HashMap<>()).getToken();

                    // 새로운 Access Token으로 인증 정보 재설정
                    Authentication authentication = tokenProvider.getAuthentication(tokenProvider.convertAuthToken(newAccessToken));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 레디스에 새로운 Refresh Token 저장
                    refreshTokenService.save(email, newRefreshToken);

                    //쿠키에 새로운 Refresh Token 저장/업데이트
                    Cookie cookie = new Cookie("refreshToken", newRefreshToken);
                    cookie.setHttpOnly(true); // JavaScript에서 접근 불가능하도록 설정
                    cookie.setPath("/"); // 쿠키가 유효한 경로 설정
                    response.addCookie(cookie); // 응답에 쿠키 추가

                    response.setHeader("Authorization", "Bearer " + newAccessToken);

                } else {
                    // Redis Token과 쿠키 Token이 다르거나 Redis Token이 존재하지 않음
                    response.sendRedirect("/users/login");
                }
            } else {
                // Refresh Token 유효하지 않음 -> 재로그인
                response.sendRedirect("/users/login");
            }
        } else {
            // Refresh Token 없음 -> 재로그인
            response.sendRedirect("/users/login");
        }
    }*/
}
