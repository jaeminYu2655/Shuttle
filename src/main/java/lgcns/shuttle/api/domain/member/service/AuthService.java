package lgcns.shuttle.api.domain.member.service;

import jakarta.servlet.http.HttpServletRequest;
import lgcns.shuttle.api.domain.member.dto.MemberRequestDto;
import lgcns.shuttle.api.domain.member.dto.MemberResponseDto;
import lgcns.shuttle.api.global.common.dto.TokenDto;


public interface AuthService {
    TokenDto login(MemberRequestDto requestDto);
    void logOut(HttpServletRequest request);
    MemberResponseDto signup(MemberRequestDto requestDto);

}
