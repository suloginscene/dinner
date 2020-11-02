package me.scene.dinner.domain.account.application;

import me.scene.dinner.domain.account.domain.*;
import me.scene.dinner.infra.environment.ActiveProfile;
import me.scene.dinner.infra.environment.URL;
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

@Service
public class AccountService implements UserDetailsService {

    private final SignupFormRepository tempRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final URL url;
    private final ActiveProfile activeProfile;

    @Autowired
    public AccountService(SignupFormRepository tempRepository, AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder, MailSender mailSender, URL url, ActiveProfile activeProfile) {
        this.tempRepository = tempRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.url = url;
        this.activeProfile = activeProfile;
    }

    @PostConstruct
    public void initAccountRepository() {
        if (activeProfile.get().equals("local")) {
            createTestAccount();
        }
    }

    private void createTestAccount() {
        SignupForm testUser = new SignupForm();
        testUser.setUsername("test");
        testUser.setEmail("email" + "@email.com");
        testUser.setPassword("testPassword");
        testUser.setAgreement(true);
        testUser.encodePassword(passwordEncoder);
        testUser.generateVerificationToken();
        accountRepository.save(new Account(testUser));
    }

    @Transactional
    public String storeInTempRepository(SignupForm signupForm) throws MessagingException {
        sendVerificationMail(signupForm);
        signupForm.encodePassword(passwordEncoder);
        signupForm = tempRepository.save(signupForm);
        return signupForm.getEmail();
    }

    private void sendVerificationMail(SignupForm signupForm) throws MessagingException {
        signupForm.generateVerificationToken();
        MailMessage mailMessage = createVerificationMailMessage(signupForm);
        mailSender.send(mailMessage);
    }

    private MailMessage createVerificationMailMessage(SignupForm signupForm) {
        String email = signupForm.getEmail();
        String verificationToken = signupForm.getVerificationToken();
        String verificationLink = String.format("%s%s?email=%s&token=%s",
                url.get(), "/verify", email, verificationToken);

        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject("[Dinner] Please verify your email address.");
        mailMessage.setTo(email);
        mailMessage.setText(verificationLink);
        return mailMessage;
    }

    @Transactional
    public String completeSignup(String email, String token) {
        if (accountRepository.findByEmail(email).isPresent()) return email + "<br><small>이미 인증된 이메일입니다.</small>";

        SignupForm signupForm = tempRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        signupForm.validateToken(token);
        Account account = accountRepository.save(new Account(signupForm));
        tempRepository.delete(signupForm);
        return account.getUsername() + " 님, 가입을 환영합니다.";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = (username.contains("@")) ?
                accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username)) :
                accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserAccount(account);
    }

    @Transactional
    public String sendNewPassword(String email) throws MessagingException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        String newRawPassword = account.changePassword(passwordEncoder);
        MailMessage mailMessage = createNewPasswordMailMessage(email, newRawPassword);
        mailSender.send(mailMessage);
        return account.getUsername();
    }

    private MailMessage createNewPasswordMailMessage(String email, String newPassword) {
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject("[Dinner] New Random Password.");
        mailMessage.setTo(email);
        mailMessage.setText(newPassword);
        return mailMessage;
    }

    @Transactional
    public Profile extractProfile(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new Profile(account);
    }

    // TODO temp version
    @Transactional
    public void changePassword(String username, String password) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        account.changePassword(password, passwordEncoder);
    }

    public String findUsernameById(Long writerId) {
        Account account = accountRepository.findById(writerId).orElse(null);
        if (account == null) return "anonymousUser";
        else return account.getUsername();
    }

}