package me.scene.paper.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.article.application.command.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TagApiController {

    private final TagService service;


    @PutMapping("/api/tags/{name}")
    public ResponseEntity<Object> createTag(@PathVariable String name) {
        service.save(name);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/tags/{name}")
    public ResponseEntity<Object> deleteTag(@PathVariable String name) {
        service.delete(name);
        return ResponseEntity.ok().build();
    }

}
