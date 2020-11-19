package me.scene.dinner.utils.factory;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.account.domain.Account;
import me.scene.dinner.account.domain.AccountRepository;
import me.scene.dinner.account.domain.TempAccount;
import me.scene.dinner.account.domain.TempAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory {

    private final AccountService accountService;
    private final TempAccountRepository tempAccountRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountFactory(AccountService accountService, TempAccountRepository tempAccountRepository, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.tempAccountRepository = tempAccountRepository;
        this.accountRepository = accountRepository;
    }

    public TempAccount createTemp(String username, String email, String password) {
        Long id = accountService.saveTemp(username, email, password);
        return tempAccountRepository.findById(id).orElseThrow();
    }

    public Account create(String username, String email, String password) {
        TempAccount temp = createTemp(username, email, password);
        Long id = accountService.transferToRegular(email, temp.getVerificationToken());
        return accountRepository.findById(id).orElseThrow();
    }

}
