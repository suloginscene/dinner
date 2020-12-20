package me.scene.dinner.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.article.application.command.ReplyService;
import me.scene.dinner.board.article.application.command.request.ReplyCreateRequest;
import me.scene.dinner.board.article.application.command.request.ReplyDeleteRequest;
import me.scene.dinner.common.security.Current;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService service;


    @PostMapping("/replies")
    public String create(@Current Account current,
                         @RequestParam Long articleId,
                         @RequestParam String content) {

        String username = current.getUsername();

        ReplyCreateRequest request = new ReplyCreateRequest(username, articleId, content);
        service.save(request);

        return "redirect:" + ("/articles/" + articleId);
    }

    @DeleteMapping("/replies")
    public String delete(@Current Account current,
                         @RequestParam Long articleId,
                         @RequestParam Long replyId) {

        String username = current.getUsername();

        ReplyDeleteRequest request = new ReplyDeleteRequest(username, articleId, replyId);
        service.delete(request);

        return "redirect:" + ("/articles/" + articleId);
    }

}
