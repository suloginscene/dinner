package me.scene.dinner.board.ui.topic;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.common.security.CurrentUser;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.application.topic.TopicDto;
import me.scene.dinner.board.application.topic.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

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
        TopicDto topic = topicService.read(topicId);
        model.addAttribute("topic", topic);
        return "page/board/topic/view";
    }

    @GetMapping("/topics/{topicId}/form")
    public String updateForm(@PathVariable Long topicId, @CurrentUser Account current, Model model) {
        TopicDto topic = topicService.findToUpdate(topicId, current.getUsername());
        model.addAttribute("id", topicId);
        model.addAttribute("updateForm", updateForm(topic));
        return "page/board/topic/update";
    }

    private TopicForm updateForm(TopicDto t) {
        TopicForm f = new TopicForm();
        f.setMagazineId(t.getMagazine().getId());
        f.setTitle(t.getTitle());
        f.setShortExplanation(t.getShortExplanation());
        f.setLongExplanation(t.getLongExplanation());
        return f;
    }

    @PutMapping("/topics/{topicId}")
    public String update(@PathVariable Long topicId, @CurrentUser Account current, @Valid TopicForm form, Errors errors) {
        if (errors.hasErrors()) return "redirect:" + ("/topics/" + topicId + "/form");

        topicService.update(topicId, current.getUsername(), form.getTitle(), form.getShortExplanation(), form.getLongExplanation());
        return "redirect:" + ("/topics/" + topicId);
    }

    @DeleteMapping("/topics/{topicId}")
    public String delete(@PathVariable Long topicId, @CurrentUser Account current) {
        Long magazineId = topicService.delete(topicId, current.getUsername());
        return "redirect:" + ("/magazines/" + magazineId);
    }

}
