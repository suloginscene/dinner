package me.scene.dinner.board.magazine.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.cache.BestMagazineCache;
import me.scene.dinner.board.magazine.application.query.MagazineQueryService;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class MagazineApiController {

    private final BestMagazineCache cache;
    private final MagazineQueryService service;


    @GetMapping("/api/best-magazines")
    public List<MagazineLink> bestMagazines() {
        return cache.getMagazines();
    }

    @GetMapping("/api/magazines/{username}")
    public List<MagazineLink> magazinesByUser(@PathVariable String username) {
        return service.linksOfUser(username);
    }

}
