package me.scene.dinner.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/tags")
    public String showTags(Model model) {
        List<String> tags = tagService.findAll();
        model.addAttribute("tags", tags);
        return "page/tag/list";
    }

    @GetMapping("/tags/{name}")
    public String showTaggedArticles(@PathVariable String name, Model model) {
        TagDto loadedTag = tagService.findLoadedTag(name);
        model.addAttribute("tag", loadedTag);
        return "page/tag/view";
    }

    @PostMapping("/api/tags/{name}")
    public @ResponseBody ResponseEntity<Object> createTag(@PathVariable String name) {
        tagService.save(name);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/tags/{name}")
    public @ResponseBody ResponseEntity<Object> deleteTag(@PathVariable String name) {
        tagService.delete(name);
        return ResponseEntity.ok().build();
    }

}
