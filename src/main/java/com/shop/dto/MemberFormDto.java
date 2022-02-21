package com.shop.dto;

import com.shop.constant.Role;
import com.shop.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
public class MemberFormDto {

    private String name;
    private String email;
    private String password;
    private String address;

    @Builder
    public MemberFormDto(String name, String email, String password, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
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
