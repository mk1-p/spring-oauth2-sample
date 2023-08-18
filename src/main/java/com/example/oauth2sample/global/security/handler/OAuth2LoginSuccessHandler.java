package com.example.oauth2sample.global.security.handler;

import com.example.oauth2sample.global.utils.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 서비스 토큰 생성
        String generateAccessToken = jwtTokenUtil.generateAccessToken(authentication);
        String generateRefreshToken = jwtTokenUtil.generateRefreshToken(authentication);

        log.info("ACCESS_TOKEN : {}",generateAccessToken);
        log.info("REFRESH_TOKEN : {}",generateRefreshToken);

        // 토큰 쿠키 설정
        Cookie accessCookie = new Cookie("access", generateAccessToken);
        accessCookie.setMaxAge(60);
        accessCookie.setPath("/token");

        Cookie refreshCookie = new Cookie("refresh", generateRefreshToken);
        refreshCookie.setMaxAge(60);
        refreshCookie.setPath("/token");

        // Response에 쿠키 추가
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        response.sendRedirect("/");

        // 클라이언트에서 쿠키를 확인하고 토큰을 로컬 스토리지에 저장한다.
        // 토큰 갱신 시 따로 요청하지 않고 쿠키에 토큰 정보가 담겨있다면 갱신하도록 한다.
    }
}
