package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.AccountService;
import me.scene.dinner.account.application.query.AccountQueryService;
import me.scene.dinner.account.application.query.dto.AccountDto;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.ui.form.PasswordForm;
import me.scene.dinner.account.ui.form.ProfileForm;
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
    private final AccountQueryService accountQueryService;

    @GetMapping("/@{username}")
    public String showInfo(@PathVariable String username, Model model) {
        AccountDto account = accountQueryService.findDto(username);
        model.addAttribute("account", account);
        return "page/account/info";
    }

    @GetMapping("/profile")
    public String profileForm(@CurrentUser Account current, Model model) {
        AccountDto account = accountQueryService.findDto(current.getUsername());
        model.addAttribute("profileForm", profileForm(account));
        return "page/account/profile";
    }

    private ProfileForm profileForm(AccountDto a) {
        ProfileForm f = new ProfileForm();
        f.setUsername(a.getUsername());
        f.setEmail(a.getEmail());
        f.setIntroduction(a.getShortIntroduction());
        return f;
    }

    @PostMapping("/profile")
    public String updateProfile(@CurrentUser Account current, @Valid ProfileForm profileForm, Errors errors) {
        if (errors.hasErrors()) return "page/account/profile";

        String username = current.getUsername();
        String introduction = profileForm.getIntroduction();
        accountService.updateProfile(username, (introduction != null) ? introduction : "");
        return "redirect:" + ("/@" + username);
    }

    @GetMapping("/password")
    public String passwordForm(Model model) {
        PasswordForm passwordForm = new PasswordForm();
        model.addAttribute("passwordForm", passwordForm);
        return "page/account/password";
    }

    @PostMapping("/password")
    public String configPrivate(@CurrentUser Account current, @Valid PasswordForm passwordForm, Errors errors) {
        if (errors.hasErrors()) return "page/account/password";

        String username = current.getUsername();
        accountService.changePassword(username, passwordForm.getPassword());
        return "redirect:" + ("/@" + username);
    }

}
