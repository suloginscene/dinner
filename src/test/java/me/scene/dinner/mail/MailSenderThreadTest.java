package me.scene.dinner.mail;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.utils.factory.AccountFactory;
import me.scene.dinner.utils.factory.MagazineFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test-mailSender")
class MailSenderThreadTest {

    @Autowired MailSenderThreadProxy mailSenderThreadProxy;

    @Autowired AccountFactory accountFactory;
    @Autowired MagazineFactory magazineFactory;

    @Autowired AccountRepository accountRepository;
    @Autowired TempAccountRepository tempAccountRepository;
    @Autowired MagazineRepository magazineRepository;

    @AfterEach
    void clear() {
        accountRepository.deleteAll();
        tempAccountRepository.deleteAll();
        magazineRepository.deleteAll();
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

    @Test
    @Transactional
    void onMemberAppliedEvent_sync() {
        Thread testThread = Thread.currentThread();

        Magazine magazine = magazineFactory.create("manager", "manager@email.com", "t", "s", "l", "MANAGED");
        magazine.applyMember("new");
        magazineRepository.save(magazine);

        Thread mailThread = mailSenderThreadProxy.lastThread();
        assertThat(mailThread).isEqualTo(testThread);
    }

    @Test
    @Transactional
    void onMemberQuitEvent_async() throws InterruptedException {
        Thread testThread = Thread.currentThread();

        Magazine magazine = magazineFactory.create("manager", "manager@email.com", "t", "s", "l", "MANAGED");
        magazine.addMember("manager", "target");
        magazine.quitMember("target");
        magazineRepository.save(magazine);

        Thread.sleep(1000L);

        Thread mailThread = mailSenderThreadProxy.lastThread();
        assertThat(mailThread).isNotNull();
        assertThat(mailThread).isNotEqualTo(testThread);
    }

}
