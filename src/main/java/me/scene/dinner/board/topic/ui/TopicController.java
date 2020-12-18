package me.scene.dinner.board.topic.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.topic.application.command.TopicService;
import me.scene.dinner.board.topic.application.command.request.TopicCreateRequest;
import me.scene.dinner.board.topic.application.command.request.TopicUpdateRequest;
import me.scene.dinner.board.topic.application.query.TopicQueryService;
import me.scene.dinner.board.topic.application.query.dto.TopicSimpleDto;
import me.scene.dinner.board.topic.ui.form.TopicForm;
import me.scene.dinner.board.topic.ui.form.TopicUpdateForm;
import me.scene.dinner.common.security.Current;
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

    private final TopicService service;
    private final TopicQueryService queryService;


    @GetMapping("/topics/{id}")
    public String showTopic(@PathVariable Long id, Model model) {
        TopicSimpleDto topic = queryService.findById(id);
        model.addAttribute("topic", topic);
        return "page/board/topic/view";
    }


    @GetMapping("/topic-form")
    public String shipTopicForm(@RequestParam Long magazineId, Model model) {
        TopicForm topicForm = new TopicForm(magazineId);
        model.addAttribute("topicForm", topicForm);
        return "page/board/topic/form";
    }

    @PostMapping("/topics")
    public String createTopic(@Current Account current, @Valid TopicForm form, Errors errors) {
        if (errors.hasErrors()) return "page/board/topic/form";

        String username = current.getUsername();
        TopicCreateRequest request = createTopicCreateRequest(username, form);
        Long id = service.save(request);
        return "redirect:" + ("/topics/" + id);
    }


    @GetMapping("/topics/{id}/form")
    public String updateForm(@PathVariable Long id, Model model) {
        TopicSimpleDto topic = queryService.findById(id);
        model.addAttribute("updateForm", updateForm(topic));
        return "page/board/topic/update";
    }

    @PutMapping("/topics/{id}")
    public String update(@PathVariable Long id, @Current Account current, @Valid TopicUpdateForm form, Errors errors) {
        if (errors.hasErrors()) return "redirect:" + ("/topics/" + id + "/form");

        String username = current.getUsername();
        TopicUpdateRequest request = createTopicUpdateRequest(username, form);
        service.update(id, request);
        return "redirect:" + ("/topics/" + id);
    }


    @DeleteMapping("/topics/{id}")
    public String delete(@PathVariable Long id, @Current Account current) {
        Long magazineId = service.delete(id, current.getUsername());
        return "redirect:" + ("/magazines/" + magazineId);
    }


    // private ---------------------------------------------------------------------------------------------------------

    private TopicCreateRequest createTopicCreateRequest(String currentUser, TopicForm f) {
        return new TopicCreateRequest(currentUser, f.getMagazineId(), f.getTitle(), f.getShortExplanation(), f.getLongExplanation());
    }

    private TopicUpdateRequest createTopicUpdateRequest(String currentUser, TopicUpdateForm f) {
        return new TopicUpdateRequest(currentUser, f.getMagazineId(), f.getTitle(), f.getShortExplanation(), f.getLongExplanation());
    }

    private TopicUpdateForm updateForm(TopicSimpleDto t) {
        return new TopicUpdateForm(t.getMagazine().getId(), t.getId(), t.getTitle(), t.getShortExplanation(), t.getLongExplanation());
    }

}
