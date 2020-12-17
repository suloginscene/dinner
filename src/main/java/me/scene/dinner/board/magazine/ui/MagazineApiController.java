package me.scene.dinner.board.magazine.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.cache.BestMagazineCache;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import me.scene.dinner.board.magazine.application.query.dto.MagazineSimpleDto;
import me.scene.dinner.board.magazine.application.query.MagazineQueryService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<MagazineLink>> bestMagazines() {
        List<MagazineLink> bestMagazines = cache.getMagazines();
        return ResponseEntity.ok(bestMagazines);
    }

    @GetMapping("/api/magazines/{username}")
    public ResponseEntity<List<MagazineSimpleDto>> magazinesByUser(@PathVariable String username) {
        List<MagazineSimpleDto> magazines = service.findByUsername(username);
        return ResponseEntity.ok(magazines);
    }

}
