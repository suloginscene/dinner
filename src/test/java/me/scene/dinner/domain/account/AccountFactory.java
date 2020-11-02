package me.scene.dinner.domain.account;

import me.scene.dinner.domain.account.domain.Account;
import me.scene.dinner.domain.account.domain.AccountRepository;
import me.scene.dinner.domain.account.domain.SignupForm;
import me.scene.dinner.domain.account.domain.SignupFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AccountFactory {

    private final PasswordEncoder passwordEncoder;
    private final SignupFormRepository tempRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountFactory(PasswordEncoder passwordEncoder, SignupFormRepository tempRepository, AccountRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.tempRepository = tempRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public SignupForm createInTemp(String username) {
        SignupForm signupForm = completeSignupForm(username);
        return tempRepository.save(signupForm);
    }

    @Transactional
    public Account createInRegular(String username) {
        SignupForm signupForm = completeSignupForm(username);
        return accountRepository.save(new Account(signupForm));
    }

    private SignupForm completeSignupForm(String username) {
        SignupForm signupForm = new SignupForm();
        signupForm.setUsername(username);
        signupForm.setEmail(username + "@email.com");
        signupForm.setPassword("password");
        signupForm.setAgreement(true);
        signupForm.encodePassword(passwordEncoder);
        signupForm.generateVerificationToken();
        return signupForm;
    }

}
