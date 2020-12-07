package me.scene.dinner.board.application.magazine;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.domain.magazine.event.MagazineChangedEvent;
import me.scene.dinner.board.domain.magazine.event.MagazineDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MagazineBestListCache {

    private final MagazineService magazineService;

    private static final int COUNT = 5;
    private List<MagazineDto> bestMagazines;


    @PostConstruct
    public void init() {
        bestMagazines = magazineService.best(COUNT);
    }


    @EventListener
    public void onApplicationEvent(MagazineChangedEvent event) {
        if ((bestMagazines.size() >= COUNT) && doesNotContain(event.getId())) return;
        bestMagazines = magazineService.best(COUNT);
    }

    @EventListener
    public void onApplicationEvent(MagazineDeletedEvent event) {
        Long id = event.getId();
        if (doesNotContain(id)) return;
        bestMagazines = magazineService.best(COUNT + 1);
        bestMagazines.remove(findTarget(id));
    }


    public List<MagazineDto> get() {
        return bestMagazines;
    }


    private boolean doesNotContain(Long id) {
        return bestMagazines.stream().noneMatch(m -> m.getId().equals(id));
    }

    public MagazineDto findTarget(Long id) {
        return bestMagazines.stream().filter(m -> m.getId().equals(id)).findFirst().orElseThrow(IllegalStateException::new);
    }

    public boolean contains(Long id) {
        return !doesNotContain(id);
    }

}
