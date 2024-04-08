package lgcns.shuttle.api.domain.member.service.impl;

import lgcns.shuttle.api.domain.member.dto.MemberResponseDto;
import lgcns.shuttle.api.domain.member.entity.Member;
import lgcns.shuttle.api.domain.member.repository.MemberRepository;
import lgcns.shuttle.api.domain.member.service.MemberService;
import lgcns.shuttle.api.global.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto getMyInfoBySecurity() {
        return memberRepository.findByNickname(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    @Transactional
    public MemberResponseDto changeMemberEmail(String nickname, String email) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
        member.setEmail(email);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto changeMemberPassword(String nickname, String exPassword, String newPassword) {
        Member member = memberRepository.findByNickname(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, member.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }

        member.setPassword(passwordEncoder.encode((newPassword)));
        return MemberResponseDto.of(memberRepository.save(member));
    }
}
