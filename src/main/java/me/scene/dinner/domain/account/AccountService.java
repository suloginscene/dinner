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
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AccountService(SignupFormRepository tempRepository, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
        this.tempRepository = tempRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Transactional
    public String storeInTempRepository(SignupForm signupForm) {
        signupForm.encodePassword(passwordEncoder);
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
        mailMessage.setText("Mail content.");
        return mailMessage;
    }

}
