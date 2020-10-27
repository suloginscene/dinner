package me.scene.dinner.domain.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignupFormValidator implements Validator {

    private final AccountRepository accountRepository;
    private final SignupFormRepository tempRepository;

    @Autowired
    public SignupFormValidator(AccountRepository accountRepository, SignupFormRepository signupFormRepository) {
        this.accountRepository = accountRepository;
        this.tempRepository = signupFormRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignupForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

        SignupForm signupForm = (SignupForm) o;
        String username = signupForm.getUsername();
        String email = signupForm.getEmail();

        if (username.equals("anonymousUser")) {
            errors.rejectValue("username", "invalid.username", "사용 할 수 없는 이름입니다.");
        }

        if (accountRepository.existsByUsername(username) || tempRepository.existsByUsername(username)) {
            errors.rejectValue("username", "duplicated.username", "이미 사용 중인 이름입니다.");
        }

        if (accountRepository.existsByEmail(email) || tempRepository.existsByEmail(email)) {
            errors.rejectValue("email", "duplicated.email", "이미 사용 중인 이메일입니다.");
        }

    }

}
