package me.scene.paper.service.board.article.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.article.application.command.TagService;
import me.scene.paper.service.board.article.application.query.TagQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class TagApiController {

    private final TagService service;
    private final TagQueryService query;

    private final ObjectMapper mapper;


    @GetMapping("/api/tags")
    public String whitelist() throws JsonProcessingException {
        List<String> names = query.names();
        return mapper.writeValueAsString(names);
    }

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
