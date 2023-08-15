package com.example.oauth2sample.global.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenUtil {

    // JWT 비밀키 (임의로 설정)
    // JWT 키는 되도록이면 길고 쉽게 풀지 못하는 것으로!
    private String ACCESS_SECRET = "aschnhkghgrrHRoiwoASqfo123kl1l23jlwfmnan19047ahnfg12rasv";
    private String REFRESH_SECRET = "elilnlk1lklkanfsio3k4jl2klkbjk3bjk23h4lkj2l3kj4las2j3l5l";


    // JWT 유효 시간 설정 (30분으로 설정)
    private long ACCESS_EXPIR = 1800;
    private long REFRESH_EXPIR = 31557600;

    TimeConverter timeConverter = new TimeConverter();


    // 토큰 타입 별 리턴
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication,ACCESS_SECRET,ACCESS_EXPIR);
    }
    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication,REFRESH_SECRET,REFRESH_EXPIR);
    }

    // JWT 토큰 생성
    private String generateToken(Authentication authentication, String secret, long expirSec) {
        // Key 세팅
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        // 현재시간, 만료시간 세팅
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusSeconds(expirSec);

        // LocalDateTime -> Date
        Date start = timeConverter.convertToDateViaInstant(now);
        Date expiryDate = timeConverter.convertToDateViaInstant(expirationTime);

        // google, kakao ...
        String clientRegistrationId = getRegistrationIdFromAuthentic(authentication);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(start)
                .setExpiration(expiryDate)
                .claim("registration_id",clientRegistrationId)
                .signWith(key,SignatureAlgorithm.HS256) //or signWith(Key, SignatureAlgorithm)
                .compact();
    }



    // JWT 토큰으로부터 사용자 ID 추출
    public String getUserIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(ACCESS_SECRET).parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    // JWT 토큰으로부터 registrationId 추출 (google, kakao)
    public String getRegistrationIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(ACCESS_SECRET).parseClaimsJws(token);
        return (String) claims.getBody().get("registration_id");
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(ACCESS_SECRET).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // RegistrationId 를 얻기 위한 메서드 (From. Authentic)
    private String getRegistrationIdFromAuthentic(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        return oauthToken.getAuthorizedClientRegistrationId();
    }

}
