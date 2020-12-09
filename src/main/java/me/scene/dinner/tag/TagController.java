package me.scene.dinner.tag;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TagController {


    @GetMapping("/tags")
    public String showTags() {
        return "page/tag/list";
    }

    @GetMapping("/tags/{name}")
    public String showTag(@PathVariable String name, Model model) {
        model.addAttribute("name", name);
        return "page/tag/view";
    }

    @PostMapping("/tags/{name}")
    public @ResponseBody ResponseEntity<Object> addTag(@PathVariable String name) {
        System.out.println(name);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tags/{name}")
    public @ResponseBody ResponseEntity<Object> deleteTag(@PathVariable String name) {
        System.out.println(name);
        return ResponseEntity.ok().build();
    }

}
