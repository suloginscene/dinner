package me.scene.dinner.board.reply.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.board.reply.application.ReplyService;
import me.scene.dinner.common.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReplyController {

    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/replies")
    public String create(@RequestParam Long articleId, @CurrentUser Account current, @RequestParam String content) {
        replyService.save(articleId, current.getUsername(), content);
        return "redirect:" + ("/articles/" + articleId);
    }

    @DeleteMapping("/replies/{replyId}")
    public String delete(@PathVariable Long replyId, @CurrentUser Account current) {
        Long articleId = replyService.delete(replyId, current.getUsername());
        return "redirect:" + ("/articles/" + articleId);
    }

}
