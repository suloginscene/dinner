package me.scene.paper.service.board.article.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.article.domain.article.model.Article;
import me.scene.paper.service.board.article.domain.article.model.ArticleTag;
import me.scene.paper.service.board.article.domain.tag.model.Tag;
import me.scene.paper.service.board.article.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static java.util.stream.Collectors.toSet;


@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository repository;


    public void save(String name) {
        if (repository.existsByName(name)) return;

        Tag tag = new Tag(name);
        repository.save(tag);
    }

    public void delete(String name) {
        Tag tag = repository.find(name);
        if (!tag.getArticleTags().isEmpty()) return;

        repository.delete(tag);
    }


    public void allocate(Article article, Set<String> tagNames) {
        Set<ArticleTag> articleTags = article.getArticleTags();

        Set<Tag> newTags = tagNames.stream().map(repository::find).collect(toSet());
        newTags.forEach(tag -> articleTags.add(new ArticleTag(article, tag)));
    }

    public void renew(Article article, Set<String> tagNames) {
        Set<ArticleTag> articleTags = article.getArticleTags();

        Set<Tag> oldTags = articleTags.stream().map(ArticleTag::getTag).collect(toSet());
        Set<Tag> newTags = tagNames.stream().map(repository::find).collect(toSet());
        oldTags.removeAll(newTags);

        oldTags.forEach(tag -> tag.getArticleTags().remove(new ArticleTag(article, tag)));
        oldTags.stream().filter(tag -> tag.getArticleTags().isEmpty()).forEach(repository::delete);

        articleTags.clear();
        newTags.forEach(tag -> articleTags.add(new ArticleTag(article, tag)));
    }

    public void release(Article article) {
        Set<ArticleTag> articleTags = article.getArticleTags();

        Set<Tag> oldTags = articleTags.stream().map(ArticleTag::getTag).collect(toSet());
        oldTags.forEach(tag -> tag.getArticleTags().remove(new ArticleTag(article, tag)));
        oldTags.stream().filter(tag -> tag.getArticleTags().isEmpty()).forEach(repository::delete);
    }

}
