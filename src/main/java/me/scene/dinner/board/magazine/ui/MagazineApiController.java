package me.scene.dinner.board.magazine.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.cache.BestMagazineCache;
import me.scene.dinner.board.magazine.application.query.MagazineQueryService;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class MagazineApiController {

    private final BestMagazineCache cache;
    private final MagazineQueryService service;


    @GetMapping("/api/magazines/best")
    public List<MagazineLink> bestMagazines() {
        return cache.getMagazines();
    }

    @GetMapping("/api/magazines")
    public List<MagazineLink> magazinesByUser(@RequestParam String username) {
        return service.linksOfUser(username);
    }

}
