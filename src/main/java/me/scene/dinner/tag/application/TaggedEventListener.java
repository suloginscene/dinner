package me.scene.dinner.tag.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.ArticleTaggedEvent;
import me.scene.dinner.tag.domain.Tag;
import me.scene.dinner.tag.domain.TagRepository;
import me.scene.dinner.tag.domain.TaggedArticle;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component @Transactional
@RequiredArgsConstructor
public class TaggedEventListener {

    private final TagRepository tagRepository;


    @EventListener
    public void onArticleTaggedEvent(ArticleTaggedEvent event) {
        Tag tag = find(event.getTagName());
        TaggedArticle taggedArticle = new TaggedArticle(event.getArticle());
        tag.addTaggedArticle(taggedArticle);
    }


    private Tag find(String name) {
        return tagRepository.findByName(name).orElseThrow();
    }

}
