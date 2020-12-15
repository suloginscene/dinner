package me.scene.dinner.account.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.event.TempAccountCreatedEvent;
import me.scene.dinner.account.application.command.event.TempPasswordIssuedEvent;
import me.scene.dinner.account.application.command.exception.AlreadyVerifiedException;
import me.scene.dinner.account.application.command.exception.VerificationException;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service @Transactional
@RequiredArgsConstructor
public class AccountService {

    private final TempAccountRepository tempRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public Long saveTemp(String username, String email, String rawPassword) {
        TempAccount tempAccount = new TempAccount(username, email, passwordEncoder.encode(rawPassword));
        Long id = tempRepository.save(tempAccount).getId();
        eventPublisher.publishEvent(new TempAccountCreatedEvent(email, tempAccount.getVerificationToken()));
        return id;
    }

    public Long transferToRegular(String email, String token) {
        if (accountRepository.existsByEmail(email)) throw new AlreadyVerifiedException(email);
        TempAccount temp = tempRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        if (!token.equals(temp.getVerificationToken())) throw new VerificationException(token);

        Account account = new Account(temp.getUsername(), temp.getEmail(), temp.getPassword());
        Long id = accountRepository.save(account).getId();
        tempRepository.delete(temp);
        return id;
    }

    public void issueTempPassword(String email) {
        Account account = findByEmail(email);
        String tempRawPassword = UUID.randomUUID().toString();
        account.changePassword(passwordEncoder.encode(tempRawPassword));
        eventPublisher.publishEvent(new TempPasswordIssuedEvent(email, tempRawPassword));
    }

    public void updateProfile(String username, String introduction) {
        Account account = find(username);
        account.update(introduction);
    }

    public void changePassword(String username, String rawPassword) {
        Account account = find(username);
        account.changePassword(passwordEncoder.encode(rawPassword));
    }

    private Account find(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

}
