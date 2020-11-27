package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.common.security.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ConfigController {

    private final AccountService accountService;

    @GetMapping("/@{username}")
    public String showInfo(@PathVariable String username, Model model) {
        Account account = accountService.find(username);
        model.addAttribute("username", account.getUsername());
        model.addAttribute("profile", account.getProfile());
        return "page/account/info";
    }

    @GetMapping("/profile")
    public String profileForm(@CurrentUser Account current, Model model) {
        Account account = accountService.find(current.getUsername());
        model.addAttribute("profileForm", profileForm(account));
        return "page/account/profile";
    }

    private ProfileForm profileForm(Account a) {
        ProfileForm f = new ProfileForm();
        f.setUsername(a.getUsername());
        f.setEmail(a.getEmail());
        String introduction = (a.getProfile() != null) ? a.getProfile().getShortIntroduction() : null;
        f.setIntroduction(introduction);
        return f;
    }

    @PostMapping("/profile")
    public String updateProfile(@CurrentUser Account current, @Valid ProfileForm profileForm, Errors errors) {
        if (errors.hasErrors()) return "page/account/profile";

        accountService.updateProfile(current.getUsername(), profileForm.getIntroduction());
        return "redirect:" + ("/@" + current.getUsername());
    }

    @GetMapping("/password")
    public String passwordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "page/account/password";
    }

    @PostMapping("/password")
    public String configPrivate(@CurrentUser Account current, @Valid PasswordForm passwordForm, Errors errors) {
        if (errors.hasErrors()) return "page/account/password";

        accountService.changePassword(current.getUsername(), passwordForm.getPassword());
        return "redirect:" + ("/@" + current.getUsername());
    }

}
