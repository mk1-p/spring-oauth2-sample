package com.example.oauth2sample.global.security.application;

import com.example.oauth2sample.domain.members.Member;
import com.example.oauth2sample.domain.members.MemberRepository;
import com.example.oauth2sample.domain.members.MemberService;
import com.example.oauth2sample.global.security.dto.CustomUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberService memberService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        // 로그인 서비스 구분 값 google, kakao, naver, apple ...
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        CustomUserInfo userInfo = CustomUserInfo.of(clientRegistration, oAuth2User.getAttributes());

        Member member = memberService.saveOrUpdate(userInfo);

        // 별도로 OAuth2 리소스 서버에 데이터를 요청하려면 인증서버에서 발급해준 Access, Refresh 토큰을 저장할 것
        // > 서비스 내 인증 토큰과 혼동X!!

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                userInfo.getAttributes(),
                userInfo.getNameAttributeKey());
    }





}
