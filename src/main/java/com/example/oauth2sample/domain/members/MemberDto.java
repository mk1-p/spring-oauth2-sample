package com.example.oauth2sample.domain.members;


import com.example.oauth2sample.domain.model.AuthType;
import com.example.oauth2sample.domain.model.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberDto {

    private Long id;
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private String name;
    private String nickname;
    private String phone;

    private String profileImage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String attributeId;         // 간편로그인 고유 아이디
    private AuthType authType;          // AuthType은 시스템 상에서만 컨트롤
    private Role role;


    @Builder
    public MemberDto(Long id, String email, String password,
                     String name, String nickname, String phone, String profileImage,
                     String attributeId, String registrationId, AuthType authType, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.profileImage = profileImage;
        this.attributeId = attributeId;
        this.authType = authType;
        this.role = role;
    }


    public static MemberDto toDto(Member entity) {
        return MemberDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
//                .password(entity.getPassword())
                .name(entity.getName())
                .nickname(entity.getNickname())
                .phone(entity.getPhone())
                .profileImage(entity.getProfileImage())
                .attributeId(entity.getAttributeId())
                .authType(entity.getAuthType())
                .role(entity.getRole())
                .build();
    }



}
