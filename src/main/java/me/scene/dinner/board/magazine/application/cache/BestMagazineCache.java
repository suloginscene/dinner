package me.scene.dinner.board.magazine.application.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.query.MagazineQueryService;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import me.scene.dinner.board.magazine.domain.common.ChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class BestMagazineCache {

    @Getter
    private List<MagazineLink> magazines = new ArrayList<>();
    private static final int SIZE = 5;

    private final MagazineQueryService queryService;


    @Scheduled(cron = "0 0 * * * *")
    protected void updateBySchedule() {
        update();
    }

    @EventListener
    public void updateByEvent(ChangedEvent event) {
        Long id = event.getId();

        boolean isFull = magazines.size() == SIZE;
        boolean contains = magazines.stream().anyMatch(m -> m.getId().equals(id));
        if (isFull && !contains) return;

        update();
    }


    private void update() {
        magazines = queryService.findBest(SIZE);
    }

}
