package me.scene.dinner.account.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.account.domain.account.model.Notification;
import me.scene.dinner.account.domain.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final AccountRepository repository;


    public void save(String username, String message) {
        Account account = repository.find(username);

        Notification notification = new Notification(username, message);
        account.getNotifications().add(notification);
    }

    public void check(String username) {
        Account account = repository.find(username);

        List<Notification> notifications = account.getNotifications();
        notifications.forEach(Notification::check);
    }

}
