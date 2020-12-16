package me.scene.dinner.tag.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.tag.application.dto.ArticleSummary;
import me.scene.dinner.tag.application.dto.TagDto;
import me.scene.dinner.tag.domain.Tag;
import me.scene.dinner.tag.domain.TagRepository;
import me.scene.dinner.tag.domain.TaggedArticle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

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

    public List<String> findAllTagNames() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(Tag::getName).collect(toList());
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


    private Tag find(String name) {
        return tagRepository.findByName(name).orElseThrow();
    }

}
