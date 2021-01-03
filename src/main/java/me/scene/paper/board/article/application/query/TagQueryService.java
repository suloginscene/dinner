package me.scene.paper.board.article.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.article.application.query.dto.TagView;
import me.scene.paper.board.article.domain.tag.model.Tag;
import me.scene.paper.board.article.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagQueryService {

    private final TagRepository repository;


    public List<String> findAllTagNames() {
        List<Tag> tags = repository.findAll();
        return tags.stream()
                .map(Tag::getName)
                .collect(toList());
    }

    public TagView view(String name) {
        Tag tag = repository.find(name);
        return new TagView(tag);
    }

}
