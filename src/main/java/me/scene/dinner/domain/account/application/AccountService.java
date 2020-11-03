package me.scene.dinner.domain.account.application;

import me.scene.dinner.domain.account.domain.Account;
import me.scene.dinner.domain.account.domain.AccountRepository;
import me.scene.dinner.domain.account.domain.TempAccount;
import me.scene.dinner.domain.account.domain.TempAccountRepository;
import me.scene.dinner.infra.environment.ActiveProfile;
import me.scene.dinner.infra.environment.URL;
import me.scene.dinner.infra.exception.UseridNotFoundException;
import me.scene.dinner.infra.exception.VerificationException;
import me.scene.dinner.infra.mail.MailMessage;
import me.scene.dinner.infra.mail.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.UUID;

@Service @Transactional(readOnly = true)
public class AccountService implements UserDetailsService {

    private final TempAccountRepository tempRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final URL url;
    private final ActiveProfile activeProfile;

    @Autowired
    public AccountService(TempAccountRepository tempRepository, AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder, MailSender mailSender, URL url, ActiveProfile activeProfile) {
        this.tempRepository = tempRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.url = url;
        this.activeProfile = activeProfile;
    }

    @PostConstruct
    public void initialAccount() {
        if (activeProfile.get().equals("local")) {
            TempAccount tempAccount = TempAccount.create("test", "test@email.com", "testPassword", passwordEncoder);
            Account account = Account.create(tempAccount);
            accountRepository.save(account);
        }
    }

    public TempAccount findTemp(Long id) {
        return tempRepository.findById(id).orElseThrow(() -> new UseridNotFoundException(id));
    }

    public Account find(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new UseridNotFoundException(id));
    }


    private void send(String subject, String to, String text) throws MessagingException {
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject(subject);
        mailMessage.setTo(to);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
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

        send("[Dinner] Please verify your email address.", email,
                "Verification Link: " + (url + "/verify?email=" + email + "&token=" + tempAccount.getVerificationToken()));
    }

    public void verify(String email, String token) {
        if (accountRepository.existsByEmail(email)) throw new VerificationException("already verified: " + email);
        TempAccount temp = tempRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        if (!token.equals(temp.getVerificationToken())) throw new VerificationException("invalid token of " + email);
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

        send("[Dinner] New Random Password.", email, "New password: " + tempRawPassword);
    }


    // ?? --------------------------------------------------------------------------------------------------------------

    @Transactional
    public AccountDto extractProfile(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new AccountDto(account);
    }

}
