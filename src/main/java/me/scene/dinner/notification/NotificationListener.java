package me.scene.dinner.notification;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.like.LikedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener {

    @EventListener @Async
    public void onLikedEvent(LikedEvent event) {
        log.info(event.getArticleId().toString());
        log.info(event.getUsername());
    }

}
