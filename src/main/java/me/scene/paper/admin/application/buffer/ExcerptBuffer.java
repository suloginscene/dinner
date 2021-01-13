package me.scene.paper.admin.application.buffer;

import lombok.RequiredArgsConstructor;
import me.scene.paper.admin.application.command.ExcerptService;
import me.scene.paper.service.home.application.cache.ExcerptCache;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
@RequiredArgsConstructor
public class ExcerptBuffer implements ExcerptCache {

    private final List<String> buffer = new ArrayList<>();
    private final Random random = new Random();

    private final ExcerptService service;


    @PostConstruct
    public void load() {
        List<String> excerpts = service.toCache();
        buffer.addAll(excerpts);
    }

    @PreDestroy
    public void persist() {
        service.toDatabase(buffer);
    }


    @Override
    public String getRandomExcerpt() {
        if (buffer.isEmpty()) return "페이퍼에 오신 것을 환영합니다.";

        int i = random.nextInt(buffer.size());
        return buffer.get(i);
    }


    public List<String> getExcerpts() {
        return buffer;
    }

    public void add(String excerpt) {
        buffer.add(excerpt);
    }

    public void remove(String excerpt) {
        buffer.remove(excerpt);
    }

}
