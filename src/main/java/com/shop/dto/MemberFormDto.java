package com.shop.dto;

import com.shop.constant.Role;
import com.shop.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class MemberFormDto {

    private String name;
    private String email;
    private String password;
    private String address;
    private Role role;

    @Builder
    public MemberFormDto(String name, String email, String password, String address, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public Member toEntity() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return Member.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .address(address)
                .role(Role.USER)
                .build();
    }
}
