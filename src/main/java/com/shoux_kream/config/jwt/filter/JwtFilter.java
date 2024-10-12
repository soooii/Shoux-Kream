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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

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
            //    handleExpiredToken(request, response, jwtToken);
                filterChain.doFilter(request, response);
                return;
            }

        }
        filterChain.doFilter(request, response);

    }
/* 만료 토큰 재발급 수정 중
    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, AuthTokenImpl jwtToken) throws IOException {
        Optional<String> refreshToken = resolveRefreshToken(request);

        // Refresh Token(쿠키) 존재하는지 확인
        if (refreshToken.isPresent()) {
            AuthTokenImpl refreshAuthToken = tokenProvider.convertAuthToken(refreshToken.get());

            Claims claims = refreshAuthToken.getDate().getClaims();
            String email = claims.getSubject();

            // DB에서 Refresh Token 조회
            Optional<RefreshToken> dbToken = refreshTokenRepository.findByEmail(email);

            // Refresh Token 유효성 검사
            if (!refreshAuthToken.validate()) {
                // Refresh Token이 유효하지 않음 -> DB에서 Refresh Token 삭제 후 재로그인
                if (dbToken.isPresent()) {refreshTokenRepository.delete(dbToken.get());}
                response.sendRedirect("/users/login");
                return;
            }

            // db Token 존재 여부와 쿠키의 Refresh Token 비교
            if (dbToken.isPresent()) {
                logger.info(dbToken.get().getRefreshToken());
                // DB에 있는 Refresh Token과 쿠키의 Refresh Token 비교
                if (dbToken.get().getRefreshToken().equals(refreshToken.get())) {
                    // 새로운 Access Token과 Refresh Token 발급

                    String roleString = claims.get("role", String.class); // String으로 가져오기
                    Role role = Role.valueOf(roleString); // Role로 변환하기
                   // String newAccessToken = tokenProvider.createAccessToken(email, role, new HashMap<>()).getToken();
                    String newAccessToken = tokenProvider.createAccessToken(email, role, claims).getToken();

                  //  String newAccessToken = tokenProvider.createAccessToken(email, claims.get("role", Role.class), new HashMap<>()).getToken();
                    logger.info(newAccessToken);

                    String newRefreshToken = tokenProvider.createRefreshToken(email, role, claims).getToken();

                 //   String newRefreshToken = tokenProvider.createRefreshToken(email, claims.get("role", Role.class), new HashMap<>()).getToken();
                    logger.info(newRefreshToken);

                    // 새로운 Access Token으로 인증 정보 재설정
                    Authentication authentication = tokenProvider.getAuthentication(tokenProvider.convertAuthToken(newAccessToken));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 새로운 Refresh Token을 DB에 저장/업데이트
                    RefreshToken newToken = dbToken.get().updateToken(newRefreshToken);
                    refreshTokenRepository.save(newToken);

                    // 쿠키에 새로운 Refresh Token 저장/업데이트
                    Cookie cookie = new Cookie("refreshToken", newRefreshToken);
                    cookie.setHttpOnly(true); // JavaScript에서 접근 불가능하도록 설정
                    cookie.setPath("/"); // 쿠키가 유효한 경로 설정
                    response.addCookie(cookie); // 응답에 쿠키 추가

                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    logger.info(String.valueOf(response));
                } else {
                    // DB에 해당 이메일을 가진 Refresh Token 존재 & 쿠키 Refresh Token과 다른 경우
                    refreshTokenRepository.delete(dbToken.get());
                    response.sendRedirect("/users/login");
                }
            } else {
                // DB에 Refresh Token이 존재하지 않는 경우 -> 재로그인
                response.sendRedirect("/users/login");
            }
        } else {
            // Refresh Token 없음(쿠키) -> 재로그인 / db에 해당 Refresh Token이 있을 경우
            // AccessToken의 sub를 가져와 -> db refresh Token 검색 -> 존재 시 삭제
            String userEmail = jwtToken.getDate().getClaims().getSubject();
            Optional<RefreshToken> dbToken = refreshTokenRepository.findByEmail(userEmail);
            if (dbToken.isPresent()) {refreshTokenRepository.delete(dbToken.get());}
            response.sendRedirect("/users/login");
        }
    }
*/

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
