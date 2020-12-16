package me.scene.dinner.tag.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.tag.application.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagApiController {

    private final TagService service;

    @PostMapping("/api/tags/{name}")
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
