package me.scene.dinner.utils.factory;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MagazineFactory {

    private final MagazineService magazineService;

    public Magazine create(String manager, String managerEmail, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Long id = magazineService.save(manager, managerEmail, title, shortExplanation, longExplanation, magazinePolicy);
        return magazineService.find(id);
    }

}
