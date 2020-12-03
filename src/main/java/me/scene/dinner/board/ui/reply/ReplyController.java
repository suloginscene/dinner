package me.scene.dinner.board.ui.reply;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.CurrentUser;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.application.reply.ReplyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

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
