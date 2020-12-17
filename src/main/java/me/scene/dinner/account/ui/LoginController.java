package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequiredArgsConstructor
public class LoginController {

    @Value("${dinner.url}")
    private String url;

    private final AccountService accountService;


    @GetMapping("/login")
    public String loginPage(HttpServletRequest req) {
        String referer = req.getHeader("Referer");
        if (RefererUtil.isMeaningful(referer, url)) {
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
        accountService.setRandomPassword(email);
        return "redirect:" + ("/sent-to-account?email=" + email);
    }


    private static class RefererUtil {

        private static boolean isMeaningful(String referer, String url) {
            if (referer == null) return false;
            if (!referer.startsWith(url)) return false;
            String ref = referer.substring(url.length());
            return !(
                    ref.equals("/") || ref.equals("/login") || ref.equals("/login?error")
                            || ref.startsWith("/verify?") || ref.startsWith("/sent-to-account?")
                            || ref.equals("/signup") || ref.equals("/forgot")
            );
        }

    }

}
