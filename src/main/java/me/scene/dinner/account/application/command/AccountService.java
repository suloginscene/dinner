package me.scene.dinner.account.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.request.ProfileUpdateRequest;
import me.scene.dinner.account.application.command.request.SignupRequest;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.account.domain.account.model.Profile;
import me.scene.dinner.account.domain.account.repository.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.model.TempAccount;
import me.scene.dinner.account.domain.tempaccount.repository.TempAccountRepository;
import me.scene.dinner.common.mail.message.MailMessage;
import me.scene.dinner.common.mail.message.MailMessageFactory;
import me.scene.dinner.common.mail.sender.MailSender;
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
    private final MailMessageFactory messageFactory;
    private final MailSender mail;


    public void signup(SignupRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();
        String rawPassword = request.getPassword();

        String encodedPassword = encoder.encode(rawPassword);

        TempAccount temp = new TempAccount(username, email, encodedPassword);
        tempRepository.save(temp);

        String token = temp.getVerificationToken();
        MailMessage message = messageFactory.verification(email, token);
        mail.send(message);
    }

    public void verify(String email, String token) {
        boolean verified = repository.existsByEmail(email);
        if (verified) return;

        TempAccount temp = tempRepository.findAccountByEmail(email);
        temp.verify(token);

        String username = temp.getUsername();
        String password = temp.getPassword();
        tempRepository.delete(temp);

        Account account = new Account(username, email, password);
        repository.save(account);
    }


    public void updateProfile(String username, ProfileUpdateRequest request) {
        String greeting = request.getGreeting();

        Profile profile = new Profile(greeting);

        Account account = repository.find(username);
        account.changeIntroduction(profile);
    }

    public void changePassword(String username, String rawPassword) {
        String encodedPassword = encoder.encode(rawPassword);

        Account account = repository.find(username);
        account.changePassword(encodedPassword);
    }

    public void setRandomPassword(String email) {
        String randomPassword = UUID.randomUUID().toString();
        String encodedPassword = encoder.encode(randomPassword);

        Account account = repository.findAccountByEmail(email);
        account.changePassword(encodedPassword);

        MailMessage message = messageFactory.randomPassword(email, randomPassword);
        mail.send(message);
    }

}
