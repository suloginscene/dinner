package me.scene.dinner.tag.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.command.ArticleTaggedEvent;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.tag.domain.TagRepository;
import me.scene.dinner.tag.domain.TaggedArticle;
import me.scene.dinner.tag.domain.TaggedArticleRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@Component
@Transactional
@RequiredArgsConstructor
public class ArticleTaggedEventListener {

    private final TaggedArticleRepository repository;
    private final TagRepository tagRepository;


    @EventListener
    public void renew(ArticleTaggedEvent event) {
        Article article = event.getArticle();
        Set<TaggedArticle> oldTaggedArticles = repository.findByArticle(article);
        repository.deleteAll(oldTaggedArticles);

        Set<String> tagNames = event.getTagNames();
        List<TaggedArticle> newTaggedArticles = tagNames.stream()
                .map(tagRepository::find)
                .map(tag -> new TaggedArticle(tag, article))
                .collect(toList());
        repository.saveAll(newTaggedArticles);
    }

}
