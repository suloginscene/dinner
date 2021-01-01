package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.AccountService;
import me.scene.dinner.account.application.command.request.ProfileUpdateRequest;
import me.scene.dinner.account.application.command.request.SignupRequest;
import me.scene.dinner.account.application.query.AccountQueryService;
import me.scene.dinner.account.application.query.dto.AccountView;
import me.scene.dinner.account.domain.account.model.Profile;
import me.scene.dinner.account.ui.form.PasswordForm;
import me.scene.dinner.account.ui.form.ProfileForm;
import me.scene.dinner.account.ui.form.SignupForm;
import me.scene.dinner.account.ui.form.SignupFormValidator;
import me.scene.dinner.common.security.Principal;
import me.scene.dinner.common.security.RefererParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;
    private final AccountQueryService query;

    private final SignupFormValidator validator;
    private final RefererParser parser;


    @InitBinder("signupForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validator);
    }


    @GetMapping("/signup")
    public String signupPage(Model model) {
        SignupForm form = new SignupForm();

        model.addAttribute("signupForm", form);
        return "page/account/signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@Valid SignupForm form, Errors errors) {
        if (errors.hasErrors()) return "page/account/signup";

        String username = form.getUsername();
        String email = form.getEmail();
        String password = form.getPassword();

        SignupRequest request = new SignupRequest(username, email, password);
        service.signup(request);

        return "redirect:" + ("/");
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String email, @RequestParam String token, Model model) {
        // TODO email to username
        String username = service.verify(email, token);

        model.addAttribute("email", email);
        return "page/account/welcome";
    }


    @GetMapping("/login")
    public String loginPage(HttpServletRequest req) {
        String referer = req.getHeader("Referer");
        if (parser.isMeaningful(referer)) {
            req.getSession().setAttribute("prev", referer);
        }
        return "page/account/login";
    }

    @GetMapping("/forgot")
    public String forgotPage() {
        return "page/account/forgot";
    }

    @PutMapping("/forgot")
    public String forgotPassword(String email) {
        service.setRandomPassword(email);
        return "redirect:" + ("/");
    }


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

    @PutMapping("/profile")
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

    @PutMapping("/password")
    public String updatePassword(@Principal String username,
                                 @Valid PasswordForm passwordForm, Errors errors) {

        if (errors.hasErrors()) return "page/account/password";

        String password = passwordForm.getPassword();
        service.changePassword(username, password);

        return "redirect:" + ("/@" + username);
    }

}
