package me.scene.dinner.domain.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final SignupFormRepository tempRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AccountService(SignupFormRepository tempRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
        this.tempRepository = tempRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Transactional
    public String storeInTempRepository(SignupForm signupForm) {
        signupForm.encodePassword(passwordEncoder);
        signupForm.generateVerificationToken();
        signupForm = tempRepository.save(signupForm);
        sendVerificationMail(signupForm);
        return signupForm.getEmail();
    }

    private void sendVerificationMail(SignupForm signupForm) {
        SimpleMailMessage mailMessage = createMailMessage(signupForm);
        javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage createMailMessage(SignupForm signupForm) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(signupForm.getEmail());
        mailMessage.setSubject("[Dinner] Verification Mail");
        mailMessage.setText(signupForm.getVerificationToken());
        return mailMessage;
    }

    @Transactional
    public String completeSignup(String email, String token) {
        SignupForm signupForm = tempRepository.findByEmail(email).orElseThrow();
        signupForm.validateToken(token);
        Account account = accountRepository.save(new Account(signupForm));
        return account.getUsername();
    }

}
