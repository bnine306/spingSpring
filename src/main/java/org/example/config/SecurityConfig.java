package org.example.config;

import org.example.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//무상태성으로 구현하기 때문에 CSRF 비활성화 해도 됨. 서버가 클라이언트 상태 기억X
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration){
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return  configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf((auth) -> auth.disable());//csrf 비활성화
        http.formLogin((auth) -> auth.disable());//기본 폼 로그인 비활성화
        http.httpBasic((auth) -> auth.disable());//기본 HTTP Basic 인증 비활성화

        //요청에 대한 인증 및 권한 부여 규칙을 설정(요청에 대해 인증 요구, 어떤 사용자에게 권한 부여할지)
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/api/product/**", "/api/products/**", "/api/signup", "/api/login").permitAll()
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/users", "/api/productinput").hasRole("ADMIN")
                .anyRequest().authenticated());//위 조건의 모든 요청에 대해 인증을 요구

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);



        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));//클라이언트에 대한 세션 무상태성으로 관리(신경쓰지 않겠다.)

        return http.build();


    }

}
