package com.example.oauth2sample.domain.members;


import com.example.oauth2sample.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import lombok.Getter;

@Getter
public class MemberDto {

    private Long id;
    private String attributeId;
    private String registrationId;
    private String password;
    private String name;
    private String nickname;
    @Email
    private String email;
    private String profileImage;
    private Role role;

}
