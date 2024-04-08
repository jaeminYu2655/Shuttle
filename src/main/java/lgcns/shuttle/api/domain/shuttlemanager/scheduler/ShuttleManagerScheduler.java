package lgcns.shuttle.api.domain.shuttlemanager.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShuttleManagerScheduler {
    private final SimpMessagingTemplate template;

    @Scheduled(cron = "0/10 * * * * *")
    public void checkConnect() {
        template.convertAndSend("/bot/1", "test");
    }
}
