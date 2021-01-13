package me.scene.paper.service.board.magazine.application.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.magazine.application.command.MagazineCache;
import me.scene.paper.service.board.magazine.application.query.MagazineQueryService;
import me.scene.paper.service.board.magazine.application.query.dto.MagazineLink;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


@Component
@RequiredArgsConstructor
public class BestMagazineCache implements MagazineCache {

    private final MagazineQueryService query;

    @Getter
    private List<MagazineLink> magazines;
    private static final int SIZE = 5;


    @PostConstruct
    public void init() {
        update();
    }


    @Override
    public void update(Long id) {
        boolean isFull = magazines.size() == SIZE;
        boolean contains = magazines.stream().anyMatch(m -> m.getId().equals(id));
        if (isFull && !contains) return;

        update();
    }

    @Scheduled(cron = "0 0 * * * *")
    protected void updateBySchedule() {
        update();
    }


    private void update() {
        magazines = query.bestLinks(SIZE);
    }

}
