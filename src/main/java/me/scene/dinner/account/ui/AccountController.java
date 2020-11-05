//package me.scene.dinner.account.ui;
//
//import me.scene.dinner.account.application.AccountDto;
//import me.scene.dinner.account.application.AccountService;
//import me.scene.dinner.account.domain.Account;
//import me.scene.dinner.common.security.CurrentUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.InitBinder;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class AccountController {
//
//    private final AccountService accountService;
//    private final AccountFormValidator accountFormValidator;
//
//    @Autowired
//    public AccountController(AccountService accountService, AccountFormValidator accountFormValidator) {
//        this.accountService = accountService;
//        this.accountFormValidator = accountFormValidator;
//    }
//
//    @InitBinder("accountForm")
//    public void initBinder(WebDataBinder webDataBinder) {
//        webDataBinder.addValidators(accountFormValidator);
//    }
//
//
//
//
//
//    @GetMapping("/@{username}")
//    public String profilePage(@PathVariable String username, @CurrentUser Account current, Model model) {
//        AccountDto accountDto = accountService.extractProfile(username);
//        model.addAttribute("accountDto", accountDto);
//
//        boolean isOwner = (current != null) && accountDto.isOwnedBy(current);
//        model.addAttribute("isOwner", isOwner);
//
//        return "page/account/profile";
//    }
//
//    // TODO update using dto...
//    @PostMapping("/@{username}")
//    public String changePassword(@PathVariable String username, @CurrentUser Account current, String password) {
//        if (current == null) throw new ForbiddenException("unauthenticated user");
//
//        String currentUsername = current.getUsername();
//        if (!currentUsername.equals(username)) throw new ForbiddenException(currentUsername);
//
//        if (password.length() < 8) {
//            String url = "/@" + username + "?short";
//            return "redirect:" + url;
//        }
//
////        accountService.changePassword(current.getEmail(), password);
//        String url = "/@" + username + "?success";
//        return "redirect:" + url;
//    }
//
//}
