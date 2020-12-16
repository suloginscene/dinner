package me.scene.dinner.mail.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MailController {

    @GetMapping("/sent-to-account")
    public String sent(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "page/mail/account";
    }

}
