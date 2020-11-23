package me.scene.dinner.account.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.account.Profile;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final TempAccountRepository tempRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = (username.contains("@")) ? findByEmail(username) : find(username);
        return new UserAccount(account);
    }

    public Account find(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Transactional
    public Long saveTemp(String username, String email, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        TempAccount tempAccount = TempAccount.create(username, email, encodedPassword);
        return tempRepository.save(tempAccount).getId();
    }

    @Transactional
    public Long transferToRegular(String email, String token) {
        if (accountRepository.existsByEmail(email)) throw new AlreadyVerifiedException(email);
        TempAccount tempAccount = tempRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        tempAccount.verify(token);
        tempRepository.delete(tempAccount);
        Account account = Account.create(tempAccount);
        return accountRepository.save(account).getId();
    }

    @Transactional
    public void issueTempPassword(String email) {
        Account account = findByEmail(email);
        String tempRawPassword = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(tempRawPassword);
        account.changePassword(encodedPassword);
        account.registerTempPasswordIssuedEvent(tempRawPassword);
        publishEvent(account);
    }

    @Transactional
    public void changeShortIntroduction(String username, String shortIntroduction) {
        Account account = find(username);
        Profile profile = new Profile(shortIntroduction);
        account.update(profile);
    }

    @Transactional
    public void changePassword(String username, String rawPassword) {
        Account account = find(username);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        account.changePassword(encodedPassword);
    }

    private void publishEvent(Account account) {
        accountRepository.save(account);
    }

}
