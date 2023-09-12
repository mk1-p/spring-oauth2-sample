package com.example.oauth2sample.domain.members;

import com.example.oauth2sample.domain.model.Role;
import com.example.oauth2sample.global.security.dto.CustomUserInfo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "MEMBER",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"attribute_id","registration_id"}    // 인증 주체와 고유 아이디
        )
)
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "attribute_id")
    private String attributeId;                 // 간편로그인 계정의 고유 ID
    @Column(name = "registration_id")
    private String registrationId;              // 간편로그인 서비스 구분 (ex : google, kakao)
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String nickname;
    @Column
    private String email;
    @Column
    private String profileImage;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(Long id, String attributeId, String registrationId, String password, String name, String nickname, String email, String profileImage, Role role) {
        this.id = id;
        this.attributeId = attributeId;
        this.registrationId = registrationId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
    }


    public static Member toEntity(CustomUserInfo userInfo) {
        return Member.builder()
                .attributeId(userInfo.getAttributeId())
                .registrationId(userInfo.getRegistrationId())
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .role(userInfo.getRole())
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
