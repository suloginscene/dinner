package me.scene.dinner.like;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/api/likes")
    void likes(@RequestParam String username, @RequestParam Long articleId) {
        likesService.likes(username, articleId);
    }

    @DeleteMapping("/api/likes")
    void dislike(@RequestParam String username, @RequestParam Long articleId) {
        likesService.dislike(username, articleId);
    }

    @GetMapping("/api/likes")
    boolean doesLike(@RequestParam String username, @RequestParam Long articleId) {
        return likesService.doesLike(username, articleId);
    }

}
