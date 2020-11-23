package me.scene.dinner.mail;

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

    @GetMapping("/sent-to-manager")
    public String sentToManager(@RequestParam Long magazineId, Model model) {
        model.addAttribute("magazineId", magazineId);
        return "page/mail/manager";
    }

}
