package me.scene.dinner.account.application.command;

import me.scene.dinner.account.application.command.request.ProfileUpdateRequest;
import me.scene.dinner.account.application.command.request.SignupRequest;
import me.scene.dinner.account.domain.account.Profile;
import me.scene.dinner.account.domain.tempaccount.TempAccountCreatedEvent;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final TempAccountRepository tempRepository;

    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher publisher;


    public void signup(SignupRequest request) {
        TempAccount tempAccount = createTempAccount(request);
        tempRepository.save(tempAccount);

        TempAccountCreatedEvent event = tempAccount.createdEvent();
        publisher.publishEvent(event);
    }


    public void verify(String email, String token) {
        if (alreadyVerified(email)) return;

        TempAccount tempAccount = findTempByEmail(email);
        tempAccount.verify(token);

        Account account = createAccount(tempAccount);
        repository.save(account);

        tempRepository.delete(tempAccount);
    }


    public void setRandomPassword(String email) {
        String randomPassword = UUID.randomUUID().toString();
        String encodedPassword = encoder.encode(randomPassword);

        Account account = findByEmail(email);
        account.changePassword(encodedPassword);

        // to Send Mail
        RandomPasswordAppliedEvent event = new RandomPasswordAppliedEvent(email, randomPassword);
        publisher.publishEvent(event);
    }


    public void updateProfile(String username, ProfileUpdateRequest request) {
        Profile profile = createProfile(request);

        Account account = find(username);
        account.changeIntroduction(profile);
    }


    public void changePassword(String username, String rawPassword) {
        String encodedPassword = encoder.encode(rawPassword);

        Account account = find(username);
        account.changePassword(encodedPassword);
    }


    // private ---------------------------------------------------------------------------------------------------------

    private Account find(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private Account findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    private TempAccount findTempByEmail(String email) {
        return tempRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }


    private TempAccount createTempAccount(SignupRequest r) {
        String encodedPassword = encoder.encode(r.getPassword());
        return new TempAccount(r.getUsername(), r.getEmail(), encodedPassword);
    }

    private Account createAccount(TempAccount t) {
        return new Account(t.getUsername(), t.getEmail(), t.getPassword());
    }

    private Profile createProfile(ProfileUpdateRequest r) {
        return new Profile(r.getGreeting());
    }


    private boolean alreadyVerified(String email) {
        Account account = repository.findByEmail(email).orElse(null);
        return account != null;
    }

}
