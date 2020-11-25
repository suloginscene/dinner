package me.scene.dinner.utils.facade;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RepositoryFacade {

    private final TempAccountRepository tempAccountRepository;
    private final AccountRepository accountRepository;

    public void deleteAll() {
        tempAccountRepository.deleteAll();
        accountRepository.deleteAll();
    }

    public Optional<TempAccount> findTempByUsername(String username) {
        return tempAccountRepository.findByUsername(username);
    }

}
