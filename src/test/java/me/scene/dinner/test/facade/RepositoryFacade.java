package me.scene.dinner.test.facade;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.board.domain.article.ArticleRepository;
import me.scene.dinner.board.domain.magazine.MagazineRepository;
import me.scene.dinner.board.domain.reply.Reply;
import me.scene.dinner.board.domain.reply.ReplyRepository;
import me.scene.dinner.board.domain.topic.TopicRepository;
import me.scene.dinner.like.LikesRepository;
import me.scene.dinner.notification.NotificationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RepositoryFacade {

    private final TempAccountRepository tempAccountRepository;
    private final AccountRepository accountRepository;

    private final MagazineRepository magazineRepository;
    private final TopicRepository topicRepository;
    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;

    private final LikesRepository likesRepository;
    private final NotificationRepository notificationRepository;

    public void deleteAll() {
        tempAccountRepository.deleteAll();
        accountRepository.deleteAll();

        replyRepository.deleteAll();
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

    public List<Reply> findAllReplies() {
        return replyRepository.findAll();
    }

}
