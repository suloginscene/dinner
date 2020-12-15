package me.scene.dinner.account.ui.form;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.query.AccountQueryService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignupFormValidator implements Validator {

    private final AccountQueryService queryService;

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

        if (queryService.existsByUsername(username) || queryService.existsTempByUsername(username)) {
            errors.rejectValue("username", "duplicated.username", "이미 사용 중인 이름입니다.");
        }

        if (queryService.existsByEmail(email) || queryService.existsTempByEmail(email)) {
            errors.rejectValue("email", "duplicated.email", "이미 사용 중인 이메일입니다.");
        }

    }

}
