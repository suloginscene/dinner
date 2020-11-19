package me.scene.dinner.account.application;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.account.domain.AccountRepository;
import me.scene.dinner.account.domain.Profile;
import me.scene.dinner.account.domain.TempAccount;
import me.scene.dinner.account.domain.TempAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service @Transactional(readOnly = true)
public class AccountService implements UserDetailsService {

    private final TempAccountRepository tempRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(TempAccountRepository tempRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.tempRepository = tempRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account find(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = (username.contains("@")) ? findByEmail(username) : find(username);
        return new UserAccount(account);
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
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

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
