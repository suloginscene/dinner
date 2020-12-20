package me.scene.dinner.notification.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.notification.domain.Notification;
import me.scene.dinner.notification.domain.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> findUnchecked(String username) {
        return notificationRepository.findByReceiverAndChecked(username, false);
    }

    public List<Notification> findChecked(String username) {
        return notificationRepository.findByReceiverAndChecked(username, true);
    }

    @Transactional
    public void check(List<Notification> notifications) {
        notifications.forEach(Notification::check);
        notificationRepository.saveAll(notifications);
    }

    public long countUnchecked(String username) {
        return notificationRepository.countByReceiverAndChecked(username, false);
    }

}
