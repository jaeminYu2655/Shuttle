package lgcns.shuttle.api.domain.member.service.impl;

import lgcns.shuttle.api.domain.member.dto.MemberResponseDto;
import lgcns.shuttle.api.domain.member.entity.Member;
import lgcns.shuttle.api.domain.member.repository.MemberRepository;
import lgcns.shuttle.api.domain.member.service.MemberService;
import lgcns.shuttle.api.global.common.repository.TokenRepository;
import lgcns.shuttle.api.global.config.SecurityConfig;
import lgcns.shuttle.api.global.jwt.JwtTokenProvider;
import net.bytebuddy.implementation.bytecode.Addition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest()
class MemberServiceImplTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberServiceImpl memberService;
    @MockBean
    MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
    }

    @Test
    @WithMockUser(username="test", authorities = {"ROLE_USER"})
    void getMyInfoBySecurity() {
        Member member = Member.builder()
                .nickname("test")
                .email("test2")
                .build();

        when(memberRepository.findByNickname(anyString()))
                .thenAnswer(re -> {
                    if (re.getArgument(0).equals("test"))
                        return Optional.of(member);
                    else
                        return Optional.empty();
                });

        // when
        MemberResponseDto memberResponseDto = memberService.getMyInfoBySecurity();

        // then
        assertEquals(MemberResponseDto.of(member).getClass(), memberResponseDto.getClass());
    }

    @Test
    @WithMockUser(username="test", authorities = {"ROLE_USER"})
    void changeMemberEmail() {
        // given
        String nickname = "test";
        String email = "test";
        String change_email = "test2";

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .build();

        Member change_member = Member.builder()
                .nickname(nickname)
                .email(change_email)
                .build();

        when(memberRepository.findByNickname(nickname))
                .thenReturn(Optional.of(member));
        when(memberRepository.save(member))
                .thenReturn(change_member);

        // when
        MemberResponseDto memberResponseDto = memberService.changeMemberEmail(nickname, change_email);

        // then
        assertEquals(nickname, memberResponseDto.getNickname());
        assertEquals(change_email, memberResponseDto.getEmail());
    }

    @Test
    @WithMockUser(username="test", authorities = {"ROLE_USER"})
    void changeMemberPassword() {
        // given
        String nickname = "test";
        String ex_password = "test";
        String new_password = "test2";

        Member member = Member.builder()
                .nickname(nickname)
                .password(passwordEncoder.encode(ex_password))
                .build();

        when(memberRepository.findByNickname(anyString())).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).then(AdditionalAnswers.returnsFirstArg());

        // when
        MemberResponseDto memberResponseDto = memberService.changeMemberPassword(nickname, ex_password, new_password);

        // then
        assertEquals(nickname, memberResponseDto.getNickname());
    }
}