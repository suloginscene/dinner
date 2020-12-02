package me.scene.dinner.notification;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.like.LikedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component @Transactional
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationRepository notificationRepository;

    private static final String ON_LIKE_MSG_TEMPLATE = "%s가 %s를 좋아합니다.";

    @EventListener @Async
    public void onLikedEvent(LikedEvent event) {
        String writer = event.getWriter();
        String user = event.getUser();
        String title = event.getTitle();
        String message = String.format(ON_LIKE_MSG_TEMPLATE, user, title);

        Notification notification = Notification.create(writer, message);
        notificationRepository.save(notification);
    }

}
