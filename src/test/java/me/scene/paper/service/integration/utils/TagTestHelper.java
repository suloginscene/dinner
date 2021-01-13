package me.scene.paper.service.integration.utils;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.article.application.command.TagService;
import me.scene.paper.service.board.article.domain.tag.repository.TagRepository;
import me.scene.paper.service.integration.utils.aop.LogAround;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TagTestHelper {

    private final TagService service;

    private final TagRepository repository;


    @LogAround
    public void createTag(String name) {
        service.save(name);
    }

    @LogAround
    public void clearTags() {
        repository.deleteAll();
    }

}
