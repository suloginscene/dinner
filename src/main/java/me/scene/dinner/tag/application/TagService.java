package me.scene.dinner.tag.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.tag.application.dto.TagView;
import me.scene.dinner.tag.domain.Tag;
import me.scene.dinner.tag.domain.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository repository;


    @Transactional
    public void save(String name) {
        if (repository.existsByName(name)) return;

        Tag tag = new Tag(name);
        repository.save(tag);
    }

    @Transactional
    public void delete(String name) {
        Tag tag = repository.find(name);
        if (!tag.getTaggedArticles().isEmpty()) return;

        repository.delete(tag);
    }

    public List<String> findAllTagNames() {
        List<Tag> tags = repository.findAll();
        return tags.stream()
                .map(Tag::getName)
                .collect(toList());
    }

    public TagView find(String name) {
        Tag tag = repository.find(name);
        return new TagView(tag);
    }

}
