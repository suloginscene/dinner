package me.scene.dinner.like.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.like.application.LikeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeService service;

    @PostMapping("/api/like")
    void likes(@RequestParam String username, @RequestParam Long articleId) {
        service.like(username, articleId);
    }

    @DeleteMapping("/api/like")
    void dislike(@RequestParam String username, @RequestParam Long articleId) {
        service.dislike(username, articleId);
    }

    @GetMapping("/api/like")
    boolean doesLike(@RequestParam String username, @RequestParam Long articleId) {
        return service.doesLike(username, articleId);
    }

}
