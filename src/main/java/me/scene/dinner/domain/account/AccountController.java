package me.scene.dinner.domain.account;

import me.scene.dinner.MainController;
import me.scene.dinner.infra.config.url.URL;
import me.scene.dinner.infra.util.RefererUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AccountController {
    public static final String URL_SIGNUP = "/signup";
    public static final String URL_VERIFY = "/verify";
    public static final String URL_LOGIN = "/login";
    public static final String URL_FORGOT = "/forgot";
    public static final String URL_PROFILE = "/accounts";

    private final AccountService accountService;
    private final SignupFormValidator signupFormValidator;
    private final URL url;

    @Autowired
    public AccountController(AccountService accountService, URL url, SignupFormValidator signupFormValidator) {
        this.accountService = accountService;
        this.signupFormValidator = signupFormValidator;
        this.url = url;
    }

    @InitBinder("signupForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signupFormValidator);
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

        if (token.equals("null")) verifyMessage = email + "<br><small>이메일 인증 후 로그인하실 수 있습니다.</small>";
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

    @PostMapping(URL_FORGOT)
    public String forgotPassword(String email) throws MessagingException {
        String username = accountService.sendNewPassword(email);
        String queryString = "?username=" + username + "&email=" + email;
        return "redirect:" + URL_FORGOT + queryString;
    }

    @GetMapping(URL_FORGOT)
    public String waitNewPassword(@RequestParam String username, @RequestParam String email, Model model) {
        String sendMessage = username + " 님의 이메일(" + email + ")로<br/>임의의 새 비밀번호를 보냈습니다.";
        model.addAttribute("sendMessage", sendMessage);
        return "page/account/forgot";
    }

    @GetMapping(URL_PROFILE + "/{username}")
    public String profilePage(@PathVariable String username, @CurrentUser Account current, Model model) {
        Profile profile = accountService.extractProfile(username);
        model.addAttribute("profile", profile);

        boolean isOwner = (current != null) && profile.isOwnedBy(current);
        model.addAttribute("isOwner", isOwner);

        return "page/account/profile";
    }

}
