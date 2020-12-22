package me.scene.dinner.account.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.query.dto.NotificationView;
import me.scene.dinner.account.domain.account.model.Notification;
import me.scene.dinner.account.domain.account.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationQueryService {

    private final AccountRepository repository;


    public long countUnchecked(String username) {
        List<Notification> notifications = repository.find(username).getNotifications();
        return notifications.stream()
                .filter(Predicate.not(Notification::isChecked))
                .count();
    }

    public NotificationView find(String username) {
        List<Notification> notifications = repository.find(username).getNotifications();
        return new NotificationView(notifications);
    }

}
