package me.scene.dinner.mail;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.utils.factory.AccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test-mailSender")
class MailSenderThreadTest {

    @Autowired MailSenderThreadProxy mailSenderThreadProxy;

    @Autowired AccountFactory accountFactory;

    @Autowired AccountRepository accountRepository;
    @Autowired TempAccountRepository tempAccountRepository;

    @BeforeEach
    void clear() {
        accountRepository.deleteAll();
        tempAccountRepository.deleteAll();
    }

    @Test
    void onTempAccountCreatedEvent_sync() {
        Thread testThread = Thread.currentThread();

        accountFactory.createTemp("username", "email@email.com", "password");

        Thread mailThread = mailSenderThreadProxy.lastThread();
        assertThat(mailThread).isEqualTo(testThread);
    }

    @Test
    void onTempPasswordIssuedEvent_sync() {
        Thread testThread = Thread.currentThread();

        Account account = accountFactory.create("username", "email@email.com", "password");
        account.registerTempPasswordIssuedEvent("tempRawPassword");
        accountRepository.save(account);

        Thread mailThread = mailSenderThreadProxy.lastThread();
        assertThat(mailThread).isEqualTo(testThread);
    }

    // TODO when need async mailing
    @Test
    @Disabled
    void onSomeEvent_async() {
        Thread testThread = Thread.currentThread();

        Thread mailThread = mailSenderThreadProxy.lastThread();
        assertThat(mailThread).isNotEqualTo(testThread);
    }

}