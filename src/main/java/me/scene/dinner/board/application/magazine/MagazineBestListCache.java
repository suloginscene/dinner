package me.scene.dinner.board.application.magazine;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.domain.magazine.Magazine;
import me.scene.dinner.board.domain.magazine.event.MagazineChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MagazineBestListCache {

    private List<Magazine> bestMagazines;
    private final MagazineService magazineService;

    // TODO scheduled cron
    // TODO count
    private void updateBestList() {
        bestMagazines = magazineService.findBest();
    }

    @PostConstruct
    private void init() {
        updateBestList();
    }

    @EventListener
    public void onApplicationEvent(MagazineChangedEvent event) {
        Magazine source = (Magazine) event.getSource();

        if (bestMagazines.contains(source) || bestMagazines.size() < 5) {
            if (event.isDeletion()) {
                // TODO count + 1
                updateBestList();
                bestMagazines.remove(source);
            } else {
                updateBestList();
            }
        }

    }

    public List<Magazine> get() {
        return bestMagazines;
    }

}
