package com.example.oauth2sample.domain.model;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final Map<String, AuthType> BY_KEY =
            Stream.of(values())
                    .collect(Collectors.toMap(AuthType::getKey, Function.identity()));

    public static AuthType valueOfKey(String insertKey) {
        return BY_KEY.get(insertKey);
    }

}
