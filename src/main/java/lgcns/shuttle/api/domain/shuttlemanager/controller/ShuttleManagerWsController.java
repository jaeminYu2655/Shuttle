package lgcns.shuttle.api.domain.shuttlemanager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ShuttleManagerWsController {
    private final SimpMessagingTemplate template;

    @MessageMapping("/test")
    public void sendTest() {
        template.convertAndSend("/bot", "test");
    }
}
