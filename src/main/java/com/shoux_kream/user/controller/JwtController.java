package com.shoux_kream.user.controller;

import com.shoux_kream.user.dto.JwtTokenDto;
import com.shoux_kream.user.dto.request.JwtTokenLoginRequest;
import com.shoux_kream.user.dto.response.JwtTokenResponse;
import com.shoux_kream.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class JwtController {
    private final UserService userService;

    //로그인
    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> jwtLogin(
            @RequestBody JwtTokenLoginRequest request,
            HttpServletResponse response
    ){
        JwtTokenDto jwtTokenResponse = userService.login(request);

        //Refresh Token 쿠키에 저장
        Cookie refreshCookie = new Cookie("refreshToken", jwtTokenResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true); //HTTPS에서만 전송되도록
        refreshCookie.setSecure(true); //쿠키 유효 경로
        refreshCookie.setPath("/"); //자바스크립트에서 접근할 수 없게
        refreshCookie.setMaxAge(7*24*60*60); //쿠키 유효 기간
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().body(JwtTokenResponse
                .builder()
                .accessToken(jwtTokenResponse.getAccessToken())
                .build()
        );
    }
}