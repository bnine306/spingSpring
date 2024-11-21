package org.example.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager){

        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/login");  // 필터 경로를 /api/login으로 변경
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String email = null;
        String password = null;

        try {
            // JSON 데이터를 읽어서 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> credentials = objectMapper.readValue(request.getReader(), Map.class);

            email = credentials.get("email"); // email 필드로 수정
            password = credentials.get("password");

            // 이메일과 패스워드 확인
            System.out.println("Received email: " + email);  // 콘솔에 이메일 출력
            System.out.println("Received password: " + password);  // 콘솔에 패스워드 출력
        } catch (IOException e) {
            throw new RuntimeException("Failed to read login request", e);
        }

        if (email == null || password == null) {
            throw new RuntimeException("Email or Password cannot be null");
        }

        // Authentication 객체를 생성하고 인증을 시도
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected  void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){

    }
}