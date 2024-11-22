package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((auth) -> auth.disable()); // CSRF 비활성화
        http.formLogin((auth) -> auth.disable()); // 기본 폼 로그인 비활성화
        http.httpBasic((auth) -> auth.disable()); // 기본 HTTP Basic 인증 비활성화

        // 모든 요청 허용
        http.authorizeHttpRequests((auth) -> auth
                .anyRequest().permitAll()); // 모든 요청에 대해 인증 없이 허용

        // 세션 무상태성으로 관리
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션을 서버에서 관리하지 않음

        return http.build();
    }
}