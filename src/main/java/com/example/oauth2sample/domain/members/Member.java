package com.example.oauth2sample.domain.members;

import com.example.oauth2sample.domain.model.AuthType;
import com.example.oauth2sample.domain.model.Role;
import com.example.oauth2sample.global.security.dto.CustomUserInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "MEMBER",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"attribute_id","auth_type"}    // 인증 주체와 고유 아이디
        )
)
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "attribute_id")
    private String attributeId;                 // 간편로그인 계정의 고유 ID
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String nickname;
    @Column
    private String phone;
    @Column
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthType authType = AuthType.LOCAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(Long id, String attributeId,
                  String email, String password, String name, String nickname, String phone,
                  String profileImage, AuthType authType, Role role) {
        this.id = id;
        this.attributeId = attributeId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.profileImage = profileImage;
        this.authType = authType;
        this.role = role;
    }

    public static Member toEntity(CustomUserInfo userInfo) {
        AuthType authType = AuthType.valueOfKey(userInfo.getRegistrationId());

        return Member.builder()
                .attributeId(userInfo.getAttributeId())
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .authType(authType)
                .role(userInfo.getRole())
                .build();
    }

    public static Member toEntity(MemberDto dto) {
        return Member.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .profileImage(dto.getProfileImage())
                .attributeId(dto.getAttributeId())
                .authType(dto.getAuthType())
                .role(dto.getRole())
                .build();
    }


    public Member updateNameAndEmail(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
