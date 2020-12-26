package me.scene.dinner.account.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.query.dto.NotificationView;
import me.scene.dinner.account.domain.account.model.Notification;
import me.scene.dinner.account.domain.account.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationQueryService {

    private final AccountRepository repository;


    public long countUnchecked(String username) {
        return repository.countUncheckedNotification(username);
    }

    public NotificationView find(String username) {
        List<Notification> notifications = repository.find(username).getNotifications();
        return new NotificationView(notifications);
    }

}
