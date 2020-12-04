package me.scene.dinner.board.application.magazine;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.domain.magazine.Magazine;
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
    private List<Magazine> bestMagazines;

    private void updateBestList(int count) {
        bestMagazines = magazineService.findBest(count);
    }

    @PostConstruct
    public void init() {
        updateBestList(COUNT);
    }

    @EventListener
    public void onApplicationEvent(MagazineChangedEvent event) {
        Magazine source = (Magazine) event.getSource();
        if ((bestMagazines.size() >= COUNT) && !(bestMagazines.contains(source))) return;
        updateBestList(COUNT);
    }

    @EventListener
    public void onApplicationEvent(MagazineDeletedEvent event) {
        Magazine source = (Magazine) event.getSource();
        if (!bestMagazines.contains(source)) return;
        updateBestList(COUNT + 1);
        bestMagazines.remove(source);
    }

    public List<Magazine> get() {
        return bestMagazines;
    }

}
