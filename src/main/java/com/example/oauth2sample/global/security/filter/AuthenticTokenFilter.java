package com.example.oauth2sample.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticTokenFilter extends OncePerRequestFilter {

    // OncePerRequestFilter 한번의 요청에 한번의 필터 검증

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;


        // token 종류 확인

        // token 토큰 유효기간 확인

        // 유효기간에 따라 갱신 여부 결정

        // Refresh Token도 만료 된 경우, 재 로그인 알림 또는 redirectUri 지정


        filterChain.doFilter(request, response);

    }
}
