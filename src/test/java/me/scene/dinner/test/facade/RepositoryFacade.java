package me.scene.dinner.test.facade;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.board.topic.domain.TopicRepository;
import me.scene.dinner.like.domain.LikeRepository;
import me.scene.dinner.notification.domain.NotificationRepository;
import me.scene.dinner.tag.domain.TagRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RepositoryFacade {

    private final TempAccountRepository tempAccountRepository;
    private final AccountRepository accountRepository;

    private final MagazineRepository magazineRepository;
    private final TopicRepository topicRepository;
    private final ArticleRepository articleRepository;

    private final LikeRepository likesRepository;
    private final NotificationRepository notificationRepository;
    private final TagRepository tagRepository;

    public void deleteAll() {
        tempAccountRepository.deleteAll();
        accountRepository.deleteAll();

        tagRepository.deleteAll();

        articleRepository.deleteAll();
        topicRepository.deleteAll();
        magazineRepository.deleteAll();

        likesRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    public void save(Account user) {
        accountRepository.save(user);
    }

    public Optional<TempAccount> findTempByUsername(String username) {
        return tempAccountRepository.findByUsername(username);
    }

}
