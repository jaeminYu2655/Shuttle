package lgcns.shuttle.api.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lgcns.shuttle.api.global.common.dto.TokenDto;
import lgcns.shuttle.api.global.common.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final TokenRepository tokenRepository;
    private static final String GRANT_TYPE = "Bearer";
    private static final String ACCESS_AUTH_HEADER = "Authorization";
    public static final String REFRESH_AUTH_HEADER = "RefreshToken";

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey
            , @Qualifier("TokenRepository") TokenRepository tokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenRepository = tokenRepository;
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenDto generateToken(Authentication authentication) {
        // Access Token 생성, 30분
        String accessToken = generateAccessToken(authentication);
        // Refresh Token 생성, 1일
        String refreshToken = generateRefreshToken(authentication);

        return TokenDto.builder()
                .grantType(GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Access Token 생성
    public String generateAccessToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);

        // 30분 설정
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(ACCESS_AUTH_HEADER, authorities)
                .setExpiration(new Date((new Date()).getTime() + 1000 * 60 * 30))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);

        // 7일 설정
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(ACCESS_AUTH_HEADER, authorities)
                .setExpiration(new Date((new Date()).getTime() + 86400000 * 7))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // https://velog.io/@juno0713/Spring-Security-JWT-React-%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%94%EB%93%9C-1.-%EA%B5%AC%EC%A1%B0-%EC%83%81%ED%83%9C%EA%B4%80%EB%A6%AC
    // Request Header 에서 토큰 정보 추출
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_AUTH_HEADER);
        return this.resolveAccessToken(bearerToken);
    }

    public String resolveAccessToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // https://velog.io/@solchan/%EB%B0%B1%EC%97%85-Refresh-Token-%EB%B0%9C%EA%B8%89%EA%B3%BC-Access-Token-%EC%9E%AC%EB%B0%9C%EA%B8%89
    public String resolveRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_AUTH_HEADER);
        if (StringUtils.hasText(refreshToken)) {
            return refreshToken;
        }
        return null;
    }

    public boolean existsRefreshToken(String refreshToken) {
        return tokenRepository.existsByRefreshToken(refreshToken);
    }

    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get(ACCESS_AUTH_HEADER) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ACCESS_AUTH_HEADER).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        // password, credentials 부분을 빈 문자열로 하는 이유는
        // 사용자가 JWT 토큰으로 인증되었기 때문에 실제 비밀번호가 필요하지 않음.
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
