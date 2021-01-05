package me.scene.paper.account.ui.form;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class PasswordFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordForm.class);
    }


    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) target;

        String password = passwordForm.getPassword();
        String passwordConfirm = passwordForm.getPasswordConfirm();

        if (!password.equals(passwordConfirm)) {
            errors.rejectValue("passwordConfirm", "invalid.passwordConfirm", "패스워드를 다시 확인해주세요.");
        }
    }

}
