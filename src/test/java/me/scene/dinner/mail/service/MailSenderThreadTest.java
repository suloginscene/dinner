package me.scene.dinner.mail.service;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.mail.infra.MailSenderThreadProxy;
import me.scene.dinner.utils.factory.AccountFactory;
import me.scene.dinner.utils.factory.MagazineFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test-mailSender")
class MailSenderThreadTest {

    Thread testThread;
    @Autowired MailSenderThreadProxy mailSenderThreadProxy;

    @Autowired AccountFactory accountFactory;
    @Autowired MagazineFactory magazineFactory;

    @Autowired AccountRepository accountRepository;
    @Autowired TempAccountRepository tempAccountRepository;
    @Autowired MagazineRepository magazineRepository;


    @BeforeEach
    void testThread() {
        testThread = Thread.currentThread();
    }

    @AfterEach
    void clear() {
        accountRepository.deleteAll();
        tempAccountRepository.deleteAll();
        magazineRepository.deleteAll();
    }


    @Test
    void onTempAccountCreatedEvent_sync() {
        accountFactory.createTemp("username", "email@email.com", "password");

        Thread mailThread = mailSenderThreadProxy.lastThread();
        assertThat(mailThread).isEqualTo(testThread);
    }

    @Test
    void onTempPasswordIssuedEvent_sync() {
        Account account = accountFactory.create("username", "email@email.com", "password");

        account.registerTempPasswordIssuedEvent("tempRawPassword");
        publishEventManually(account);

        Thread mailThread = mailSenderThreadProxy.lastThread();
        assertThat(mailThread).isEqualTo(testThread);
    }

    @Test
    @Transactional
    void onMemberAppliedEvent_sync() {
        Magazine magazine = magazineFactory.create("manager", "manager@email.com", "t", "s", "l", "MANAGED");

        magazine.applyMember("new");
        publishEventManually(magazine);

        Thread mailThread = mailSenderThreadProxy.lastThread();
        assertThat(mailThread).isEqualTo(testThread);
    }

    @Test
    @Transactional
    void onMemberQuitEvent_async() throws InterruptedException {
        Magazine magazine = magazineFactory.create("manager", "manager@email.com", "t", "s", "l", "MANAGED");
        magazine.addMember("manager", "target");

        magazine.quitMember("target");
        publishEventManually(magazine);
        Thread.sleep(1000L);

        Thread mailThread = mailSenderThreadProxy.lastThread();
        assertThat(mailThread).isNotNull();
        assertThat(mailThread).isNotEqualTo(testThread);
    }


    private void publishEventManually(Account account) {
        accountRepository.save(account);
    }

    private void publishEventManually(Magazine magazine) {
        magazineRepository.save(magazine);
    }

}
