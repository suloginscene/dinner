package me.scene.dinner.test.facade;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.Policy;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.test.factory.AccountFactory;
import me.scene.dinner.test.factory.MagazineFactory;
import me.scene.dinner.test.factory.TopicFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FactoryFacade {

    private final AccountFactory accountFactory;
    private final MagazineFactory magazineFactory;
    private final TopicFactory topicFactory;

    public TempAccount createTempAccount(String username) {
        return accountFactory.createTemp(username, username + "@email.com", "password");
    }

    public Account createAccount(String username) {
        return accountFactory.create(username, username + "@email.com", "password");
    }

    public Magazine createMagazine(Account manager, String title, Policy policy) {
        return magazineFactory.create(manager.getUsername(), manager.getEmail(),
                title, title + "에 관한 짧은 소개", title + "에 관한 긴 소개", policy.name());
    }

    public Topic createTopic(Magazine magazine, Account manager, String title) {
        return topicFactory.create(magazine.getId(), manager.getUsername(),
                title, title + "에 관한 짧은 소개", title + "에 관한 긴 소개");
    }

}
