package lgcns.shuttle.api.domain.member.controller;

import lgcns.shuttle.api.domain.member.dto.ChangePasswordRequestDto;
import lgcns.shuttle.api.domain.member.dto.MemberRequestDto;
import lgcns.shuttle.api.domain.member.dto.MemberResponseDto;
import lgcns.shuttle.api.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        MemberResponseDto myInfoBySecurity = memberService.getMyInfoBySecurity();
        return ResponseEntity.ok((myInfoBySecurity));
    }

    @PostMapping("/nickname")
    public ResponseEntity<MemberResponseDto> setMemberNickname(@RequestBody MemberRequestDto request) {
        return ResponseEntity.ok(memberService.changeMemberEmail(request.getNickname(), request.getEmail()));
    }

    @PostMapping("/password")
    public ResponseEntity<MemberResponseDto> setMemberPassword(@RequestBody ChangePasswordRequestDto request) {
        return ResponseEntity.ok(memberService.changeMemberPassword(request.getNickname(), request.getExPassword(), request.getNewPassword()));
    }
}
