package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.account.ui.form.AccountForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountFormValidator implements Validator {

    private final AccountRepository accountRepository;
    private final TempAccountRepository tempAccountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(AccountForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

        AccountForm accountForm = (AccountForm) o;
        String username = accountForm.getUsername();
        String email = accountForm.getEmail();

        if (username.equals("anonymousUser")) {
            errors.rejectValue("username", "invalid.username", "사용 할 수 없는 이름입니다.");
        }

        if (accountRepository.existsByUsername(username) || tempAccountRepository.existsByUsername(username)) {
            errors.rejectValue("username", "duplicated.username", "이미 사용 중인 이름입니다.");
        }

        if (accountRepository.existsByEmail(email) || tempAccountRepository.existsByEmail(email)) {
            errors.rejectValue("email", "duplicated.email", "이미 사용 중인 이메일입니다.");
        }

    }

}
