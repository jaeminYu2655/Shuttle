package lgcns.shuttle.api.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {
    // JWT 인증 Type
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
