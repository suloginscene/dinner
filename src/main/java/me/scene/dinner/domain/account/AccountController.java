package me.scene.dinner.domain.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {
    public static final String URL_SIGNUP = "/signup";
    public static final String URL_LOGIN = "/login";

    @GetMapping(URL_SIGNUP)
    public String signupPage(Model model) {
        model.addAttribute(new SignupForm());
        return "page/account/signup";
    }

    @PostMapping(URL_SIGNUP)
    public String signupSubmit(@Valid SignupForm signupForm, Errors errors) {
        if (errors.hasErrors()) return "page/account/signup";

        return "redirect:/";
    }

    @GetMapping(URL_LOGIN)
    public String login() {
        return "page/account/login";
    }

}
