package me.scene.dinner.test.factory;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.application.magazine.MagazineService;
import me.scene.dinner.board.domain.magazine.Magazine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MagazineFactory {

    private final MagazineService magazineService;

    public Magazine create(String manager, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Long id = magazineService.save(manager, title, shortExplanation, longExplanation, magazinePolicy);
        return magazineService.find(id);
    }

}
