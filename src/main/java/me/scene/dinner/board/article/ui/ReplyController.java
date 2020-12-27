package me.scene.dinner.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.command.ReplyService;
import me.scene.dinner.board.article.application.command.request.ReplyCreateRequest;
import me.scene.dinner.board.article.application.command.request.ReplyDeleteRequest;
import me.scene.dinner.common.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService service;


    @PostMapping("/replies")
    public String create(@Principal String username,
                         @RequestParam Long articleId,
                         @RequestParam String content) {

        ReplyCreateRequest request = new ReplyCreateRequest(username, articleId, content);
        service.save(request);

        return "redirect:" + ("/articles/" + articleId);
    }

    @DeleteMapping("/replies")
    public String delete(@Principal String username,
                         @RequestParam Long articleId,
                         @RequestParam Long replyId) {

        ReplyDeleteRequest request = new ReplyDeleteRequest(username, articleId, replyId);
        service.delete(request);

        return "redirect:" + ("/articles/" + articleId);
    }

}
