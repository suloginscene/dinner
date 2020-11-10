package me.scene.dinner.board.magazine.ui;

import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MagazineRestController {

    private final MagazineService magazineService;

    @Autowired
    public MagazineRestController(MagazineService magazineService) {
        this.magazineService = magazineService;
    }

    @GetMapping("/api/magazines")
    public ResponseEntity<List<Magazine>> magazineList() {
        // TODO findBest
        return ResponseEntity.ok(magazineService.findAll());
    }

}
