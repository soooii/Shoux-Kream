package com.shoux_kream.config;

import com.shoux_kream.exception.JwtAccessDeniedHandler;
import com.shoux_kream.exception.JwtAuthenticationEntryPoint;
import com.shoux_kream.config.jwt.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final UserDetailsService userService;
    private final JwtFilter jwtFilter;

    //1.스프링 시큐리티 비활성화
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }


    //2.특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) throws Exception {
        return http.
                exceptionHandling(ex->ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, SecurityPath.ONLY_GET_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST, SecurityPath.ONLY_POST_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.GET, UserSecurityPath.ONLY_GET_WHITELIST).hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST, UserSecurityPath.ONLY_POST_WHITELIST).hasAuthority("USER")
                        .requestMatchers(HttpMethod.PATCH, UserSecurityPath.ONLY_PATCH_WHITELIST).hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, UserSecurityPath.ONLY_DELETE_WHITELIST).hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, AdminSecurityPath.ONLY_GET_WHITELIST).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, AdminSecurityPath.ONLY_POST_WHITELIST).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, AdminSecurityPath.ONLY_PATCH_WHITELIST).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, AdminSecurityPath.ONLY_DELETE_WHITELIST).hasAuthority("ADMIN")
                        .requestMatchers("/js/**", "/css/**", "/html/**","/img/**").permitAll()
                        .anyRequest().authenticated())
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("https://jrqggzccfxaqcbkg.tunnel-pt.elice.io/","http://localhost:9000", "https://txqfegberfyqzheq.tunnel-pt.elice.io/")); // 허용할 도메인
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH")); // 허용할 메서드
                    config.setAllowCredentials(true); // 인증 정보 포함 여부
                    config.setAllowedHeaders(List.of("*")); // 허용할 헤더
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



    // 7. 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        return authenticationManagerBuilder.build();
    }


    @Bean // 9. 패스워드 인코더로 사용할 빈 등록
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

