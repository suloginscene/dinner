package me.scene.dinner.account.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.account.application.query.dto.AccountDto;
import me.scene.dinner.account.application.query.dto.AccountView;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountQueryService implements UserDetailsService {

    private final AccountRepository repository;
    private final TempAccountRepository tempRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = (username.contains("@")) ? findByEmail(username) : find(username);
        return new UserAccount(account);
    }


    public AccountDto accountDto(String username) {
        Account account = find(username);
        return new AccountDto(account);
    }

    public AccountView accountView(String username) {
        Account account = find(username);
        return new AccountView(account);
    }


    public boolean globalExistsByUsername(String username) {
        Account account = repository.findByUsername(username).orElse(null);
        if (account != null) return true;

        TempAccount temp = tempRepository.findByUsername(username).orElse(null);
        return temp != null;
    }

    public boolean globalExistsByEmail(String email) {
        Account account = repository.findByEmail(email).orElse(null);
        if (account != null) return true;

        TempAccount temp = tempRepository.findByEmail(email).orElse(null);
        return temp != null;
    }


    // private ---------------------------------------------------------------------------------------------------------

    private Account find(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private Account findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

}
