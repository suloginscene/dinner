package me.scene.dinner.utils.factory;

import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MagazineFactory {

    private final MagazineService magazineService;

    @Autowired
    public MagazineFactory(MagazineService magazineService) {
        this.magazineService = magazineService;
    }

    public Magazine create(String manager, String managerEmail, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Long id = magazineService.save(manager, managerEmail, title, shortExplanation, longExplanation, magazinePolicy);
        return magazineService.find(id);
    }

}
