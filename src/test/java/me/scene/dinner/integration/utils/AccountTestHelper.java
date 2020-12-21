package me.scene.dinner.integration.utils;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.AccountService;
import me.scene.dinner.account.application.command.request.SignupRequest;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.integration.utils.aop.LogAround;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AccountTestHelper {

    private final AccountService service;

    private final AccountRepository repository;
    private final TempAccountRepository tempRepository;


    @LogAround
    public void createAccount(String username, String email, String password) {
        SignupRequest request = new SignupRequest(username, email, password);
        service.signup(request);

        TempAccount temp = tempRepository.findAccountByEmail(email);
        String verificationToken = temp.getVerificationToken();
        service.verify(email, verificationToken);
    }

    @LogAround
    public void clearAccounts() {
        repository.deleteAll();
        tempRepository.deleteAll();
    }

}
