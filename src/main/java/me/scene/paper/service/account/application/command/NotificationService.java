package me.scene.paper.service.account.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.account.domain.account.model.Account;
import me.scene.paper.service.account.domain.account.model.Notification;
import me.scene.paper.service.account.domain.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final AccountRepository repository;


    public void save(String username, String message) {
        Account account = repository.find(username);
        account.addNotification(new Notification(username, message));
    }

    public void check(String username) {
        Account account = repository.find(username);
        account.checkNotifications();
    }

}
