package me.scene.dinner.account.application;

import me.scene.dinner.account.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.UUID;

@Service @Transactional(readOnly = true)
public class AccountService implements UserDetailsService {

    @Value("${dinner.url}")
    private String url;

    private final TempAccountRepository tempRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Autowired
    public AccountService(TempAccountRepository tempRepository, AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.tempRepository = tempRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }


    // signup ----------------------------------------------------------------------------------------------------------

    @Transactional
    public Long saveTemp(String username, String email, String password) {
        TempAccount tempAccount = TempAccount.create(username, email, password, passwordEncoder);
        tempAccount = tempRepository.save(tempAccount);
        return tempAccount.getId();
    }

    public void sendVerificationMail(String email) throws MessagingException {
        TempAccount tempAccount = tempRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        mailSender.send("[Dinner] Please verify your email address.", email,
                "Verification Link: " + (url + "/verify?email=" + email + "&token=" + tempAccount.getVerificationToken()));
    }

    public void verify(String email, String token) {
        if (accountRepository.existsByEmail(email)) throw new AlreadyVerifiedException(email);
        TempAccount temp = tempRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        temp.verify(token);
    }

    @Transactional
    public Long transferFromTempToRegular(String email) {
        TempAccount tempAccount = tempRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        Account account = Account.create(tempAccount);
        account = accountRepository.save(account);
        tempRepository.delete(tempAccount);
        return account.getId();
    }


    // login -----------------------------------------------------------------------------------------------------------

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = (username.contains("@")) ?
                accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username)) :
                accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserAccount(account);
    }

    @Transactional
    public void sendTempPassword(String email) throws MessagingException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        String tempRawPassword = UUID.randomUUID().toString();
        account.changePassword(passwordEncoder.encode(tempRawPassword));

        mailSender.send("[Dinner] New Random Password.", email, "New password: " + tempRawPassword);
    }


    // profile --------------------------------------------------------------------------------------------------------------

    public Account find(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public void changeShortIntroduction(String username, String shortIntroduction) {
        Account account = find(username);
        Profile profile = new Profile(shortIntroduction);
        account.update(profile);
    }

    @Transactional
    public void changePassword(String username, String password) {
        Account account = find(username);
        account.changePassword(passwordEncoder.encode(password));
    }

}
