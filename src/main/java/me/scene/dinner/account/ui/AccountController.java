package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.AccountService;
import me.scene.dinner.account.application.command.request.ProfileUpdateRequest;
import me.scene.dinner.account.application.query.AccountQueryService;
import me.scene.dinner.account.application.query.dto.AccountView;
import me.scene.dinner.account.domain.account.model.Profile;
import me.scene.dinner.account.ui.form.PasswordForm;
import me.scene.dinner.account.ui.form.ProfileForm;
import me.scene.dinner.common.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;
    private final AccountQueryService query;


    @GetMapping("/@{username}")
    public String account(@PathVariable String username, Model model) {
        AccountView account = query.accountView(username);

        model.addAttribute("account", account);
        return "page/account/view";
    }


    @GetMapping("/profile")
    public String profileForm(@Principal String username, Model model) {
        AccountView account = query.accountView(username);
        Profile profile = account.getProfile();

        ProfileForm profileForm = new ProfileForm(
                account.getUsername(),
                account.getEmail(),
                profile.getGreeting()
        );

        model.addAttribute("profileForm", profileForm);
        return "page/account/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Principal String username,
                                @Valid ProfileForm form, Errors errors) {

        if (errors.hasErrors()) return "page/account/profile";

        ProfileUpdateRequest request = new ProfileUpdateRequest(form.getGreeting());
        service.updateProfile(username, request);

        return "redirect:" + ("/@" + username);
    }


    @GetMapping("/password")
    public String passwordForm(Model model) {
        PasswordForm passwordForm = new PasswordForm();

        model.addAttribute("passwordForm", passwordForm);
        return "page/account/password";
    }

    @PostMapping("/password")
    public String updatePassword(@Principal String username,
                                 @Valid PasswordForm passwordForm, Errors errors) {

        if (errors.hasErrors()) return "page/account/password";

        String password = passwordForm.getPassword();
        service.changePassword(username, password);

        return "redirect:" + ("/@" + username);
    }

}
