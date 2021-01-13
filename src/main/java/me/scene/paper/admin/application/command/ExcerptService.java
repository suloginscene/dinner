package me.scene.paper.admin.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.paper.admin.domain.model.Excerpt;
import me.scene.paper.admin.domain.repository.ExcerptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@Transactional
@RequiredArgsConstructor
public class ExcerptService {

    private final ExcerptRepository repository;


    public List<String> toCache() {
        List<Excerpt> excerpts = repository.findAll();

        List<String> strings = excerpts.stream()
                .map(Excerpt::getExcerpt)
                .collect(toList());

        repository.deleteAll();

        return strings;
    }


    public void toDatabase(List<String> buffer) {
        List<Excerpt> excerpts = buffer.stream()
                .map(Excerpt::new)
                .collect(toList());

        repository.saveAll(excerpts);
    }

}
