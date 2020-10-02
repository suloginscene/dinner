package me.scene.dinner.domain.board.topic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TopicController {

    public static final String URL = "/topics";

    @GetMapping(URL + "/{title}")
    public String readAnArticle(@PathVariable String title, Model model) {
        model.addAttribute("title", title);
        return "page/board/topic/view";
    }

}
