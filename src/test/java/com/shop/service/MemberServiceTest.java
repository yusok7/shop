package com.shop.service;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    public Member createMember() {
        MemberFormDto memberFormDto = MemberFormDto.builder()
                .name("홍길동")
                .email("test@gmail.com")
                .address("경기도 남양주시")
                .password("1234")
                .role(Role.USER)
                .build();
        return memberFormDto.toEntity();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void saveMemberTest() {

        // given
        Member member = createMember();

        // when
        Member savedMember = memberService.saveMember(member);

        // then
        assertThat(member.getName()).isEqualTo(savedMember.getName());
        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(member.getAddress()).isEqualTo(savedMember.getAddress());
        assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(member.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    void saveDuplicateMemberTest() {

        // given
        Member member1 = createMember();
        Member member2 = createMember();

        // when
        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

        // then
        assertThat(e.getMessage()).isEqualTo("이미 가입된 회원입니다.");
    }
}