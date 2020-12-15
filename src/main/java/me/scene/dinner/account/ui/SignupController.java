package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.AccountService;
import me.scene.dinner.account.ui.form.SignupForm;
import me.scene.dinner.account.ui.form.SignupFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final AccountService accountService;
    private final SignupFormValidator signupFormValidator;

    @InitBinder("signupForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signupFormValidator);
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

        String email = form.getEmail();
        accountService.saveTemp(form.getUsername(), email, form.getPassword());
        return "redirect:" + ("/sent-to-account?email=" + email);
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String email, @RequestParam String token, Model model) {
        accountService.transferToRegular(email, token);
        model.addAttribute("email", email);
        return "page/account/welcome";
    }

}
