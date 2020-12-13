package me.scene.dinner.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.application.article.ArticleTaggedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    private Tag find(String name) {
        return tagRepository.findByName(name).orElseThrow();
    }

    public List<String> findAll() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(Tag::getName).collect(toList());
    }

    @Transactional
    public void save(String name) {
        if (tagRepository.existsByName(name)) return;
        tagRepository.save(Tag.create(name));
    }

    @Transactional
    public void delete(String name) {
        Tag tag = find(name);
        // TODO check deletable
        tagRepository.delete(tag);
    }

    @EventListener
    @Transactional
    public void onArticleTaggedEvent(ArticleTaggedEvent event) {
        Tag tag = find(event.getTagName());
        TaggedArticle taggedArticle = TaggedArticle.create(event.getArticle());
        tag.addTaggedArticle(taggedArticle);
    }

    public Set<TaggedArticle> taggedArticles(String name) {
        Tag tag = find(name);
        // TODO fetch join
        Set<TaggedArticle> taggedArticles = tag.getTaggedArticles();
        log.debug("load: {}", taggedArticles);
        return taggedArticles;
    }

}
