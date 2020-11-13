package me.scene.dinner.board.topic.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.board.topic.application.TopicService;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.common.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }


    @GetMapping("/topic-form")
    public String shipTopicForm(@RequestParam Long magazineId, Model model) {
        TopicForm topicForm = new TopicForm();
        topicForm.setMagazineId(magazineId);
        model.addAttribute("topicForm", topicForm);
        return "page/board/topic/form";
    }

    @PostMapping("/topics")
    public String createTopic(@CurrentUser Account current, @Valid TopicForm form, Errors errors) {
        if (errors.hasErrors()) return "page/board/topic/form";

        Long id = topicService.save(form.getMagazineId(), current.getUsername(), form.getTitle(), form.getShortExplanation(), form.getLongExplanation());
        return "redirect:" + ("/topics/" + id);
    }

    @GetMapping("/topics/{topicId}")
    public String showTopic(@PathVariable Long topicId, Model model) {
        model.addAttribute("topic", topicService.find(topicId));
        return "page/board/topic/view";
    }

    @GetMapping("/topics/{topicId}/form")
    public String updateForm(@PathVariable Long topicId, @CurrentUser Account current, Model model) {
        Topic topic = topicService.find(topicId);
        topic.confirmManager(current.getUsername());

        model.addAttribute("updateForm", updateForm(topic));
        return "page/board/topic/update";
    }

    private TopicForm updateForm(Topic t) {
        TopicForm f = new TopicForm();
        f.setId(t.getId());
        f.setMagazineId(t.getMagazine().getId());
        f.setTitle(t.getTitle());
        f.setShortExplanation(t.getShortExplanation());
        f.setLongExplanation(t.getLongExplanation());
        return f;
    }

    @PutMapping("/topics/{topicId}")
    public String update(@PathVariable Long topicId, @CurrentUser Account current, @Valid TopicForm form, Errors errors) {
        // TODO js validation
        if (errors.hasErrors()) return "redirect:" + ("/topics/" + topicId + "/form");

        topicService.update(topicId, current.getUsername(), form.getTitle(), form.getShortExplanation(), form.getLongExplanation());
        return "redirect:" + ("/topics/" + topicId);
    }

}
