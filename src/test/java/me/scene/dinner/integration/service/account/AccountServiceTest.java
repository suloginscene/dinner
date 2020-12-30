package me.scene.dinner.integration.service.account;

import me.scene.dinner.account.application.command.AccountService;
import me.scene.dinner.account.application.command.request.SignupRequest;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.account.domain.account.repository.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.model.TempAccount;
import me.scene.dinner.account.domain.tempaccount.repository.TempAccountRepository;
import me.scene.dinner.common.mail.message.MailMessage;
import me.scene.dinner.common.mail.message.MailMessageFactory;
import me.scene.dinner.common.mail.sender.MailSender;
import me.scene.dinner.integration.utils.AccountTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;


@SpringBootTest
@DisplayName("Account_service")
class AccountServiceTest {

    @Autowired AccountService service;

    @Autowired TempAccountRepository tempRepository;
    @Autowired AccountRepository repository;

    @Autowired PasswordEncoder encoder;

    @Autowired MailMessageFactory messageFactory;
    @SpyBean MailSender mail;

    @Autowired AccountTestHelper helper;


    @AfterEach
    void clear() {
        helper.clearAccounts();
    }


    @Nested class OnSignup {
        @Test
        void encodes_and_publishes() {
            SignupRequest request = new SignupRequest("username", "email@email.com", "password");
            service.signup(request);

            TempAccount temp = tempRepository.findAccountByEmail("email@email.com");
            assertThat(temp.getPassword()).matches(encoded -> encoder.matches("password", encoded));

            MailMessage message = messageFactory.verification("email@email.com", temp.getVerificationToken());
            then(mail).should().send(message);
        }
    }

    @Nested class OnVerify {
        @Test
        void transfers() {
            service.signup(new SignupRequest("username", "email@email.com", "password"));
            String verificationToken = tempRepository.findAccountByEmail("email@email.com").getVerificationToken();

            service.verify("email@email.com", verificationToken);

            Account account = repository.find("username");
            assertThat(account.getPassword()).matches(encoded -> encoder.matches("password", encoded));

            assertThrows(UsernameNotFoundException.class,
                    () -> tempRepository.findAccountByEmail("email@email.com")
            );
        }
    }


    @Nested class OnChangePassword {
        @Test
        void encodes() {
            helper.createAccount("username", "email@email.com", "password");

            service.changePassword("username", "newPassword");

            String password = repository.find("username").getPassword();
            assertThat(password).matches(encoded -> encoder.matches("newPassword", encoded));
        }

        @Nested class WithoutParam {
            @Test
            void encodes_and_publishes() {
                helper.createAccount("username", "email@email.com", "password");

                service.setRandomPassword("email@email.com");

                String password = repository.find("username").getPassword();
                assertThat(password).startsWith("{bcrypt}");

                then(mail).should(atLeastOnce()).send(any());
            }
        }
    }

}
