package me.scene.dinner.board.magazine.application.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.command.MagazineCache;
import me.scene.dinner.board.magazine.application.query.MagazineQueryService;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class BestMagazineCache implements MagazineCache {

    @Getter
    private List<MagazineLink> magazines = new ArrayList<>();
    private static final int SIZE = 5;

    private final MagazineQueryService query;


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


    public void update() {
        magazines = query.bestLinks(SIZE);
    }

}
