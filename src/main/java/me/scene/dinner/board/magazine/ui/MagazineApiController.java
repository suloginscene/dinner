package me.scene.dinner.board.magazine.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.MagazineBestListCache;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.application.MagazineSimpleDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MagazineApiController {

    private final MagazineService magazineService;
    private final MagazineBestListCache bestListCache;

    @GetMapping("/api/best-magazines")
    public List<MagazineSimpleDto> bestList() {
        return bestListCache.getBestMagazines();
    }

    @GetMapping("/api/magazines/{username}")
    public List<MagazineSimpleDto> byUser(@PathVariable String username) {
        return magazineService.findByManager(username);
    }

}
