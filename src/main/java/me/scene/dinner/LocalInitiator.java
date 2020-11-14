package me.scene.dinner;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.account.domain.Account;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.topic.application.TopicService;
import me.scene.dinner.board.topic.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LocalInitiator implements ApplicationRunner {

    private final Environment environment;
    private final AccountService accountService;
    private final MagazineService magazineService;
    private final TopicService topicService;
    private final ArticleService articleService;

    @Autowired
    public LocalInitiator(Environment environment, AccountService accountService, MagazineService magazineService, TopicService topicService, ArticleService articleService) {
        this.environment = environment;
        this.accountService = accountService;
        this.magazineService = magazineService;
        this.topicService = topicService;
        this.articleService = articleService;
    }

    @Override
    public void run(ApplicationArguments args) {
        String activeProfile = environment.getActiveProfiles()[0];
        if (!activeProfile.equals("local")) return;

        Account account1 = registerInitialUser("ta1", "ta1@email.com", "password1");
        Account account2 = registerInitialUser("ta2", "ta2@email.com", "password2");
        Account account3 = registerInitialUser("ta3", "ta3@email.com", "password3");
        Magazine magazine1 = registerInitialMagazine(account1.getUsername(), "tm1", "short1", "long1", "OPEN");
        Magazine magazine2 = registerInitialMagazine(account2.getUsername(), "tm2", "short2", "long2", "MANAGED");
        Magazine magazine3 = registerInitialMagazine(account3.getUsername(), "tm3", "short3", "long3", "EXCLUSIVE");
        Topic topic1 = registerInitialTopic(magazine1.getId(), account1.getUsername(), "tt1", "short1", "long1");
        Topic topic2 = registerInitialTopic(magazine2.getId(), account2.getUsername(), "tt2", "short2", "long2");
        Topic topic3 = registerInitialTopic(magazine3.getId(), account3.getUsername(), "tt3", "short3", "long3");
        registerInitialArticle(topic1.getId(), account1.getUsername(), "ta1", "tc1");
        registerInitialArticle(topic2.getId(), account2.getUsername(), "ta2", "tc2");
        registerInitialArticle(topic3.getId(), account3.getUsername(), "ta3", "tc3");
    }

    private Account registerInitialUser(String username, String email, String password) {
        accountService.saveTemp(username, email, password);
        accountService.transferFromTempToRegular(email);
        return accountService.find(username);
    }

    private Magazine registerInitialMagazine(String manager, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Long id = magazineService.save(manager, title, shortExplanation, longExplanation, magazinePolicy);
        return magazineService.find(id);
    }

    private Topic registerInitialTopic(Long magazineId, String manager, String title, String shortExplanation, String longExplanation) {
        Long id = topicService.save(magazineId, manager, title, shortExplanation, longExplanation);
        return topicService.find(id);
    }

    private void registerInitialArticle(Long topicId, String writer, String title, String content) {
        Long id = articleService.save(topicId, writer, title, content);
        articleService.publish(id, writer);
    }

}
