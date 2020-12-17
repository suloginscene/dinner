package me.scene.dinner.board.magazine.application.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.command.MagazineUpdatedEvent;
import me.scene.dinner.board.magazine.application.query.MagazineQueryService;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BestMagazineCache {

    @Getter
    private List<MagazineLink> magazines;
    private final static int SIZE = 5;

    private final MagazineQueryService queryService;


    @Scheduled(cron = "0 0 * * * *")
    public void cron() {
        update();
    }

    @EventListener
    public void onMagazineUpdatedEvent(MagazineUpdatedEvent event) {
        Long id = event.getId();
        if (!isFull() || contains(id)) {
            update();
        }
    }


    private void update() {
        this.magazines = queryService.findBest(SIZE);
    }


    private boolean isFull() {
        return magazines.size() >= SIZE;
    }

    private boolean contains(Long id) {
        return magazines.stream().map(MagazineLink::getId).collect(toList()).contains(id);
    }

}
