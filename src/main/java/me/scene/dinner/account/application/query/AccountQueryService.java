package me.scene.dinner.account.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.query.dto.AccountDto;
import me.scene.dinner.account.application.query.dto.UserAccount;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountQueryService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final TempAccountRepository tempRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = (username.contains("@")) ? findByEmail(username) : find(username);
        return new UserAccount(account);
    }

    public AccountDto findDto(String username) {
        Account account = find(username);
        return extractDto(account);
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public boolean existsTempByUsername(String username) {
        return tempRepository.existsByUsername(username);
    }

    public boolean existsTempByEmail(String email) {
        return tempRepository.existsByEmail(email);
    }

    private Account find(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    private AccountDto extractDto(Account a) {
        return new AccountDto(a.getUsername(), a.getEmail(), a.shortIntroduction());
    }

}
