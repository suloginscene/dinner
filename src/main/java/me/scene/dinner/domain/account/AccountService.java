package me.scene.dinner.domain.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final SignupFormRepository tempRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(SignupFormRepository tempRepository, PasswordEncoder passwordEncoder) {
        this.tempRepository = tempRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String storeInTempRepository(SignupForm signupForm) {
        signupForm.encodePassword(passwordEncoder);
        signupForm = tempRepository.save(signupForm);
        // TODO send email for verification
        return signupForm.getEmail();
    }

}
