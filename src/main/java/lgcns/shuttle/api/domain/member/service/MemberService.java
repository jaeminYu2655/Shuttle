package lgcns.shuttle.api.domain.member.service;

import lgcns.shuttle.api.domain.member.dto.MemberResponseDto;

public interface MemberService {
    MemberResponseDto getMyInfoBySecurity();
    MemberResponseDto changeMemberEmail(String nickname, String email);
    MemberResponseDto changeMemberPassword(String nickname, String exPassword, String newPassword) ;
}
