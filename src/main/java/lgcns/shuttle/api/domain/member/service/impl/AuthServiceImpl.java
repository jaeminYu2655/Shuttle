package lgcns.shuttle.api.domain.member.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lgcns.shuttle.api.domain.member.dto.MemberRequestDto;
import lgcns.shuttle.api.domain.member.dto.MemberResponseDto;
import lgcns.shuttle.api.domain.member.entity.Member;
import lgcns.shuttle.api.domain.member.repository.MemberRepository;
import lgcns.shuttle.api.domain.member.service.AuthService;
import lgcns.shuttle.api.global.common.dto.TokenDto;
import lgcns.shuttle.api.global.common.entity.RefreshToken;
import lgcns.shuttle.api.global.common.repository.TokenRepository;
import lgcns.shuttle.api.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    @Transactional(value = "jpaTxManager")
    public TokenDto login(MemberRequestDto requestDto) {
        // 1. Login ID / PW 기반으로 Authentication 객체 생성
        // authenticated 값은 false (인증 여부 확인)
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        // 2. 실제 검증 - 비밀번호 체크
        // CustomUserDetailService. loadUserByUsername 메서드 실행
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        tokenRepository.save(new RefreshToken(tokenDto.getRefreshToken()));

        return tokenDto;
    }

    @Transactional(value = "jpaTxManager")
    public void logOut(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        if(tokenRepository.existsByRefreshToken(refreshToken)) {
            tokenRepository.delete(new RefreshToken(refreshToken));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // access Token black list 설정 필요
    }

    @Transactional(value = "jpaTxManager")
    public MemberResponseDto signup(MemberRequestDto requestDto) {
        if (memberRepository.existsByNickname(requestDto.getNickname())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Member member = requestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }
}
