package me.scene.dinner.utils.factory;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountFactory {

    private final AccountService accountService;
    private final TempAccountRepository tempAccountRepository;
    private final AccountRepository accountRepository;

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
