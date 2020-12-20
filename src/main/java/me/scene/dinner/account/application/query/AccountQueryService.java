package me.scene.dinner.account.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.query.dto.AccountInfo;
import me.scene.dinner.account.application.query.dto.AccountView;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
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
        Account account = (username.contains("@"))
                ? repository.findAccountByEmail(username)
                : repository.find(username);
        return new UserAccount(account);
    }


    public AccountInfo accountInfo(String username) {
        Account account = repository.find(username);
        return new AccountInfo(account);
    }

    public AccountView accountView(String username) {
        Account account = repository.find(username);
        return new AccountView(account);
    }


    public boolean globalExistsByUsername(String username) {
        return repository.existsByUsername(username) || tempRepository.existsByUsername(username);
    }

    public boolean globalExistsByEmail(String email) {
        return repository.existsByEmail(email) || tempRepository.existsByEmail(email);
    }

}
