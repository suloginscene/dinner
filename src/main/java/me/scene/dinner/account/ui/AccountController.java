package me.scene.dinner.account.ui;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.account.domain.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class AccountController {

    @Value("${dinner.url}")
    private String url;

    private final AccountService accountService;
    private final AccountFormValidator accountFormValidator;

    @Autowired
    public AccountController(AccountService accountService, AccountFormValidator accountFormValidator) {
        this.accountService = accountService;
        this.accountFormValidator = accountFormValidator;
    }

    @InitBinder("accountForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountFormValidator);
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        AccountForm accountForm = new AccountForm();
        model.addAttribute("accountForm", accountForm);
        return "page/account/signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@Valid AccountForm form, Errors errors) {
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

    @GetMapping("/login")
    public String loginPage(HttpServletRequest req) {
        String referer = req.getHeader("Referer");
        if (isMeaningful(referer)) {
            req.getSession().setAttribute("prev", referer);
        }
        return "page/account/login";
    }

    @GetMapping("/forgot")
    public String forgotPage() {
        return "page/account/forgot";
    }

    @PostMapping("/forgot")
    public String forgotPassword(String email) {
        accountService.issueTempPassword(email);
        return "redirect:" + ("/sent-to-account?email=" + email);
    }

    @GetMapping("/api/accounts/{username}")
    public @ResponseBody ResponseEntity<Account> find(@PathVariable String username) {
        return accountService.existsByUsername(username) ?
                ResponseEntity.ok(accountService.find(username)) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/api/email/{username}")
    public @ResponseBody ResponseEntity<String> findEmail(@PathVariable String username) {
        return accountService.existsByUsername(username) ?
                ResponseEntity.ok(accountService.find(username).getEmail()) :
                ResponseEntity.notFound().build();
    }

    private boolean isMeaningful(String referer) {
        if (referer == null) return false;
        String ref = referer.substring(url.length());
        return !(
                ref.equals("/") || ref.equals("/login") || ref.equals("/login?error")
                        || ref.startsWith("/verify?") || ref.startsWith("/sent-to-account?")
                        || ref.equals("/signup") || ref.equals("/forgot")
        );
    }

}
