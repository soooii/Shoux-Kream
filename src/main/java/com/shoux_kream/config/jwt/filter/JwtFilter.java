package com.shoux_kream.config.jwt.filter;

import com.shoux_kream.config.jwt.JwtTokenResolver;
import com.shoux_kream.config.jwt.impl.AuthTokenImpl;
import com.shoux_kream.config.jwt.impl.JwtProviderImpl;
import com.shoux_kream.exception.JwtAuthenticationEntryPoint;
import com.shoux_kream.user.service.JwtBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.Optional;

import static com.shoux_kream.config.jwt.UserConstants.AUTHORIZATION_TOKEN_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProviderImpl tokenProvider;
    private final JwtTokenResolver tokenResolver;
    private final JwtBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = tokenResolver.resolveToken(request);

        if (token.isPresent()) {
            AuthTokenImpl jwtToken = tokenProvider.convertAuthToken(token.get().split(" ")[1]);
            String jti = jwtToken.getDate().getId();

            if (!blacklistService.isBlacklisted(jti) && jwtToken.validate()) {
                Authentication authentication = tokenProvider.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request, response);
    }


}
