package me.scene.dinner.domain.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    public static final String URL_SIGNUP = "/signup";
    public static final String URL_LOGIN = "/login";

    @GetMapping(URL_SIGNUP)
    public String signup() {
        return "page/account/signup";
    }

    @GetMapping(URL_LOGIN)
    public String login() {
        return "page/account/login";
    }

}
