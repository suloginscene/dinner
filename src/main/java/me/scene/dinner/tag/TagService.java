package me.scene.dinner.tag;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.ArticleTaggedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
        tagRepository.save(new Tag(name));
    }

    @Transactional
    public void delete(String name) {
        Tag tag = find(name);
        if (!tag.getTaggedArticles().isEmpty()) return;
        tagRepository.delete(tag);
    }

    @EventListener
    @Transactional
    public void onArticleTaggedEvent(ArticleTaggedEvent event) {
        Tag tag = find(event.getTagName());
        TaggedArticle taggedArticle = new TaggedArticle(event.getArticle());
        tag.addTaggedArticle(taggedArticle);
    }

    public TagDto findLoadedTag(String name) {
        Tag tag = find(name);
        // TODO fetch join
        return new TagDto(name,
                tag.getPublicizedTaggedArticles().stream()
                        .map(TaggedArticle::getArticle)
                        .map(a -> new ArticleSummary(a.getId(), a.getTitle()))
                        .collect(toList()));
    }

}
