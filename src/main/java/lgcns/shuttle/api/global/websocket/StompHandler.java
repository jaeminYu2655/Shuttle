package lgcns.shuttle.api.global.websocket;

import lgcns.shuttle.api.global.jwt.JwtTokenProvider;
import lgcns.shuttle.api.global.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.core.Ordered;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);


        if ( StompCommand.CONNECT == accessor.getCommand()) {
            // Connect인 경우에만 Token 검증
            String accessToken = jwtTokenProvider.resolveAccessToken(accessor.getFirstNativeHeader("authorization"));
            if (!jwtTokenProvider.validateToken(accessToken))
                throw new RuntimeException("Access Token 인증 실패");
        }
        else if (StompCommand.SEND == accessor.getCommand() ) {
            log.info("SEND");
        }

        return message;
    }
}
