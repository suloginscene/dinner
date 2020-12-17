package me.scene.dinner.board.article.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.article.query.dto.ArticleSimpleDto;
import me.scene.dinner.board.article.query.dto.TopicSummary;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.Reply;
import me.scene.dinner.board.common.BoardNotFoundException;
import me.scene.dinner.board.common.Owner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component @Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleQueryService {

    private final ArticleQueryRepository articleQueryRepository;


    @Transactional
    public ArticleSimpleDto read(Long id, String current) {
        Article article = find(id);
        if (article.isPublicized()) article.read();
        else article.getOwner().identify(current);
        return extractDto(article);
    }

    public ArticleSimpleDto findToUpdate(Long id, String current) {
        Article article = find(id);
        article.getOwner().identify(current);
        return extractDto(article);
    }

    private ArticleSimpleDto extractDto(Article a) {
        // TODO fetch
        List<Reply> replies = a.getReplies();
        log.info("load: {}", replies);

        return new ArticleSimpleDto(a.getId(), a.getOwner().getOwnerName(), a.getTitle(), a.getContent(), a.isPublicized(),
                a.getCreatedAt(), a.getRead(), a.getLikes(), new TopicSummary(a.getTopic()), replies);
    }

    public List<ArticleSimpleDto> findPublicByWriter(String username) {
        List<Article> articles = articleQueryRepository.findByOwnerAndPublicizedOrderByPointDesc(new Owner(username), true);
        return articles.stream().map(this::extractDto).collect(Collectors.toList());
    }

    public List<ArticleSimpleDto> findPrivateByWriter(String username) {
        List<Article> articles = articleQueryRepository.findByOwnerAndPublicizedOrderByCreatedAtAsc(new Owner(username), false);
        return articles.stream().map(this::extractDto).collect(Collectors.toList());
    }

    private Article find(Long id) {
        return articleQueryRepository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }

}
