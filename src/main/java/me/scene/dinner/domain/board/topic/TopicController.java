package me.scene.dinner.domain.board.topic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TopicController {

    public static final String FORM = "/topic-form";
    public static final String URL = "/topics";

    @GetMapping(FORM)
    public String shipTopicForm() {
        return "page/board/topic/form";
    }

    @GetMapping(URL + "/{title}")
    public String showTopic(@PathVariable String title, Model model) {
        model.addAttribute("title", title);
        return "page/board/topic/view";
    }

}
