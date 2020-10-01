package me.scene.dinner.domain.account;

import org.springframework.beans.factory.annotation.Autowired;
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

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(URL_SIGNUP)
    public String signupPage(Model model) {
        model.addAttribute(new SignupForm());
        return "page/account/signup";
    }

    @PostMapping(URL_SIGNUP)
    public String signupSubmit(@Valid SignupForm signupForm, Errors errors, Model model) {
        if (errors.hasErrors()) return "page/account/signup";

        String email = accountService.storeInTempRepository(signupForm);
        model.addAttribute("emailSendingMessage", email + "로 회원가입 인증메일을 보냈습니다.");
        return "page/account/emailSend";
    }

    @GetMapping(URL_LOGIN)
    public String login() {
        return "page/account/login";
    }

}
