package me.scene.dinner.test.facade;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.Status;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.Policy;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.test.factory.AccountFactory;
import me.scene.dinner.test.factory.ArticleFactory;
import me.scene.dinner.test.factory.MagazineFactory;
import me.scene.dinner.test.factory.ReplyFactory;
import me.scene.dinner.test.factory.TopicFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FactoryFacade {

    private final AccountFactory accountFactory;
    private final MagazineFactory magazineFactory;
    private final TopicFactory topicFactory;
    private final ArticleFactory articleFactory;
    private final ReplyFactory replyFactory;

    public TempAccount createTempAccount(String username) {
        return accountFactory.createTemp(username, username + "@email.com", "password");
    }

    public Account createAccount(String username) {
        return accountFactory.create(username, username + "@email.com", "password");
    }

    public Magazine createMagazine(Account manager, String title, Policy policy) {
        return magazineFactory.create(manager.getUsername(), title, title + "에 관한 짧은 소개", title + "에 관한 긴 소개", policy.name());
    }

    public Topic createTopic(Magazine magazine, Account manager, String title) {
        return topicFactory.create(magazine.getId(), manager.getUsername(), title, title + "에 관한 짧은 소개", title + "에 관한 긴 소개");
    }

    public Article createArticle(Topic topic, Account writer, String title, Status status) {
        return articleFactory.create(topic.getId(), writer.getUsername(), title, title + "의 본문", status.name());
    }

    public Long createReply(Article article, Account writer, String content) {
        return replyFactory.create(article.getId(), writer.getUsername(), content);
    }

}
