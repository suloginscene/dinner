package me.scene.dinner.domain.account;

import me.scene.dinner.MainController;
import me.scene.dinner.infra.config.url.URL;
import me.scene.dinner.infra.util.RefererUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AccountController {
    public static final String URL_SIGNUP = "/signup";
    public static final String URL_VERIFY = "/verify";
    public static final String URL_LOGIN = "/login";

    private final AccountService accountService;
    private final URL url;

    @Autowired
    public AccountController(AccountService accountService, URL url) {
        this.accountService = accountService;
        this.url = url;
    }

    @GetMapping(URL_SIGNUP)
    public String signupPage(Model model) {
        model.addAttribute(new SignupForm());
        return "page/account/signup";
    }

    @PostMapping(URL_SIGNUP)
    public String signupSubmit(@Valid SignupForm signupForm, Errors errors) throws MessagingException {
        if (errors.hasErrors()) return "page/account/signup";

        String email = accountService.storeInTempRepository(signupForm);
        String queryString = "?email=" + email + "&token=" + null;
        return "redirect:" + URL_VERIFY + queryString;
    }

    @GetMapping(URL_VERIFY)
    public String verifyEmail(@RequestParam String email, @RequestParam String token, Model model) {
        String verifyMessage;

        if (token.equals("null")) verifyMessage = email + "로 인증메일을 보냈습니다.";
        else verifyMessage = accountService.completeSignup(email, token);

        model.addAttribute("verifyMessage", verifyMessage);
        return "page/account/verify";
    }

    @GetMapping(URL_LOGIN)
    public String loginPage(HttpServletRequest request) {
        rememberRedirectingUrl(request);
        return "page/account/login";
    }

    private void rememberRedirectingUrl(HttpServletRequest request) {
        String referer = RefererUtils.parse(request, url.get());
        String redirectTo = (RefererUtils.contextless(referer)) ? MainController.URL_HOME : referer;

        HttpSession session = request.getSession();
        session.setAttribute("redirectTo", redirectTo);
    }

}
