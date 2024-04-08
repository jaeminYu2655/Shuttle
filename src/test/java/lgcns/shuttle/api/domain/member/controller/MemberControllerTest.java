package lgcns.shuttle.api.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lgcns.shuttle.api.domain.member.dto.ChangePasswordRequestDto;
import lgcns.shuttle.api.domain.member.dto.MemberRequestDto;
import lgcns.shuttle.api.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("나에 대한 정보 조회")
    @WithMockUser(username="test", authorities = {"ROLE_USER"})
    void getMyMemberInfo() throws Exception{
        mvc.perform(get("/member/me"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Nickname 변경")
    @WithMockUser(username="test", authorities = {"ROLE_USER"})
    void setMemberNickname() throws Exception {
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .nickname("test")
                .password("test")
                .email("test2")
                .build();

        mvc.perform(post("/member/nickname")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Password 변경")
    @WithMockUser(username="test", authorities = {"ROLE_USER"})
    void setMemberPassword() throws Exception {
        // given
        ChangePasswordRequestDto memberRequestDto = ChangePasswordRequestDto.builder()
                .nickname("test")
                .exPassword("test")
                .newPassword("test2")
                .build();

        mvc.perform(post("/member/password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isOk());
    }
}