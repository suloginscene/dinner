package me.scene.dinner.board.magazine.application;

import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class MagazineBestListCache {

    private List<Magazine> bestMagazines;
    private final MagazineService magazineService;

    @Autowired
    public MagazineBestListCache(MagazineService magazineService) {
        this.magazineService = magazineService;
    }

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
        System.out.println("onApplicationEvent!!!");
        Magazine source = event.getSource();

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
