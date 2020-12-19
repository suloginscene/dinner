package me.scene.dinner.board.magazine.application.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.command.MagazineUpdatedEvent;
import me.scene.dinner.board.magazine.application.query.MagazineQueryService;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BestMagazineCache {

    @Getter
    private List<MagazineLink> magazines;
    private static final int SIZE = 5;

    private final MagazineQueryService queryService;


    @Scheduled(cron = "0 0 * * * *")
    protected void cron() {
        update();
    }

    @EventListener
    public void onMagazineUpdatedEvent(MagazineUpdatedEvent event) {
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
