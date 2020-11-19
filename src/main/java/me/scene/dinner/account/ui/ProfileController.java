package me.scene.dinner.account.ui;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.common.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ProfileController {

    private final AccountService accountService;

    @Autowired
    public ProfileController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/@{username}")
    public String showProfile(@PathVariable String username, @CurrentUser Account current, Model model) {
        model.addAttribute("isOwner", (current != null) && (current.getUsername().equals(username)));
        model.addAttribute("account", accountService.find(username));
        return "page/account/profile";
    }

    @GetMapping("/config")
    public String configure(@CurrentUser Account current, Model model) {
        model.addAttribute("account", current);
        model.addAttribute("profileForm", new ProfileForm());
        model.addAttribute("passwordForm", new PasswordForm());
        return "page/account/config";
    }

    @PostMapping("/public")
    public String configPublic(@CurrentUser Account current, @Valid ProfileForm profileForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", current);
            model.addAttribute("passwordForm", new PasswordForm());
            return "page/account/config";
        }

        accountService.changeShortIntroduction(current.getUsername(), profileForm.getShortIntroduction());
        return "redirect:" + ("/@" + current.getUsername());
    }

    @PostMapping("/private")
    public String configPrivate(@CurrentUser Account current, @Valid PasswordForm passwordForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", current);
            model.addAttribute("profileForm", new ProfileForm());
            return "page/account/config";
        }

        accountService.changePassword(current.getUsername(), passwordForm.getPassword());
        return "redirect:" + ("/@" + current.getUsername());
    }

}
