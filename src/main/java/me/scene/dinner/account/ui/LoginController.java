package me.scene.dinner.account.ui;

import me.scene.dinner.account.application.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final AccountService accountService;

    @Autowired
    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("referer", req.getHeader("Referer"));
        return "page/account/login";
    }

    @GetMapping("/forgot")
    public String forgotPage() {
        return "page/account/forgot";
    }

    @PostMapping("/forgot")
    public String forgotPassword(String email) {
        accountService.issueTempPassword(email);
        return "redirect:" + ("/sent?email=" + email);
    }

}
