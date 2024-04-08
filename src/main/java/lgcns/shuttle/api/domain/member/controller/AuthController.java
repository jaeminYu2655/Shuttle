package lgcns.shuttle.api.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lgcns.shuttle.api.domain.member.dto.MemberRequestDto;
import lgcns.shuttle.api.domain.member.dto.MemberResponseDto;
import lgcns.shuttle.api.domain.member.service.AuthService;
import lgcns.shuttle.api.global.common.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    //    public ResponseEntity<TokenDto> login(@RequestParam("nickname") String nickname,@RequestParam("password") String password) {
    //    MemberRequestDto requestDto = new MemberRequestDto(nickname, password, null);
    @PostMapping(value="/login")
    @Operation(summary = "로그인", description = "로그인을 진행합니다.", tags = {"auth"})
    public ResponseEntity<Void> login(@RequestBody MemberRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        TokenDto tokenDto = authService.login(requestDto);
        headers.setBearerAuth(tokenDto.getAccessToken());
        headers.add("RefreshToken", tokenDto.getRefreshToken());

        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping(value="/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.", tags = {"auth"})
    public ResponseEntity<Void> logout(HttpServletRequest servletRequest) {
        authService.logOut(servletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value="/signup")
    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.", tags = {"auth"})
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }
}