package me.scene.paper.admin.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.common.utility.LinkConverter;
import me.scene.paper.admin.application.buffer.ExcerptBuffer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ExcerptController {

    private final ExcerptBuffer buffer;

    private final LinkConverter linkConverter;


    @GetMapping("/admin/excerpt")
    public String excerptPage(Model model) {
        List<String> excerpts = buffer.getExcerpts();

        model.addAttribute("excerpts", excerpts);
        return "page/admin/excerpt";
    }

    @PostMapping("/admin/excerpt")
    public String addExcerpt(@RequestParam Long id, @RequestParam String text) {
        String excerpt = linkConverter.excerpt(id, text);
        buffer.add(excerpt);

        return "redirect:" + ("/admin/excerpt");
    }

    @DeleteMapping("/admin/excerpt")
    public String removeExcerpt(@RequestParam String excerpt) {
        buffer.remove(excerpt);

        return "redirect:" + ("/admin/excerpt");
    }

}
