package me.scene.dinner.domain.account.utils;

import me.scene.dinner.domain.account.application.AccountService;
import me.scene.dinner.domain.account.domain.Account;
import me.scene.dinner.domain.account.domain.TempAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory {

    private final AccountService accountService;

    @Autowired
    public AccountFactory(AccountService accountService) {
        this.accountService = accountService;
    }

    public TempAccount createTemp(String username, String email, String password) {
        Long id = accountService.saveTemp(username, email, password);
        return accountService.findTemp(id);
    }

    public Account create(String username, String email, String password) {
        accountService.saveTemp(username, email, password);
        Long id = accountService.transferFromTempToRegular(email);
        return accountService.find(id);
    }

}
