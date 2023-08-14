package com.example.oauth2sample.domain.model;

import lombok.Getter;

@Getter
public enum AuthType {
    GOOGLE("google","구글 간편로그인"),
    KAKAO("kakao","카카오 간편로그인"),
    APPLE("apple","애플 간편로그인"),
    LOCAL("local","로컬 간편 로그인")
    ;


    private String key;
    private String desc;


    AuthType(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
