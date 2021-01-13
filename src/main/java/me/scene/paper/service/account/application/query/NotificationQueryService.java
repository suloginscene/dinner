package me.scene.paper.service.account.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.account.application.query.dto.NotificationView;
import me.scene.paper.service.account.domain.account.model.Account;
import me.scene.paper.service.account.domain.account.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationQueryService {

    private final AccountRepository repository;


    public long countUnchecked(String username) {
        return repository.countUncheckedNotification(username);
    }

    public NotificationView find(String username) {
        Account account = repository.find(username);
        return new NotificationView(account.getNotifications());
    }

}
