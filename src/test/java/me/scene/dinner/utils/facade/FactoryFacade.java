package me.scene.dinner.utils.facade;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.utils.factory.AccountFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FactoryFacade {

    private final AccountFactory accountFactory;

    public TempAccount createTempAccount(String username) {
        return accountFactory.createTemp(username, username + "@email.com", "password");
    }

    public Account createAccount(String username) {
        return accountFactory.create(username, username + "@email.com", "password");
    }

}
