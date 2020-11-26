package me.scene.dinner.test.facade;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.board.reply.domain.ReplyRepository;
import me.scene.dinner.board.topic.domain.TopicRepository;
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
    private final ReplyRepository replyRepository;

    public void deleteAll() {
        tempAccountRepository.deleteAll();
        accountRepository.deleteAll();

        replyRepository.deleteAll();
        articleRepository.deleteAll();
        topicRepository.deleteAll();
        magazineRepository.deleteAll();
    }

    public Optional<TempAccount> findTempByUsername(String username) {
        return tempAccountRepository.findByUsername(username);
    }

    public Optional<Magazine> findMagazineByTitle(String title) {
        return magazineRepository.findByTitle(title);
    }

}
