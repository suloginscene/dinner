package me.scene.dinner.board.article.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.domain.tag.model.Tag;
import me.scene.dinner.board.article.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        if (!tag.getArticleTags().isEmpty()) return;

        repository.delete(tag);
    }

}
