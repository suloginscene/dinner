package me.scene.dinner.mail.ui;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountCreatedEvent;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.board.magazine.domain.MemberQuitEvent;
import me.scene.dinner.mail.service.AsyncMessagingException;
import me.scene.dinner.mail.service.MailSender;
import me.scene.dinner.mail.service.SyncMessagingException;
import me.scene.dinner.test.utils.authentication.WithAccount;
import me.scene.dinner.test.factory.AccountFactory;
import me.scene.dinner.test.factory.MagazineFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class MailSenderExceptionTest {

    @MockBean MailSender mailSender;

    @Autowired MockMvc mockMvc;

    @Autowired AccountFactory accountFactory;
    @Autowired MagazineFactory magazineFactory;

    @Autowired TempAccountRepository tempAccountRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired MagazineRepository magazineRepository;

    @AfterEach
    void clear() {
        tempAccountRepository.deleteAll();
        accountRepository.deleteAll();
        magazineRepository.deleteAll();
    }

    @Test
    void onSyncMessagingException_rollback() throws Exception {
        Mockito.doThrow(SyncMessagingException.class).when(mailSender).onApplicationEvent(any(TempAccountCreatedEvent.class));

        mockMvc.perform(
                post("/signup")
                        .with(csrf())
                        .param("username", "scene")
                        .param("email", "scene@email.com")
                        .param("password", "password")
                        .param("agreement", "true")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/messaging"))
        ;
        TempAccount account = tempAccountRepository.findByUsername("scene").orElse(null);
        assertThat(account).isNull();
    }

    @Test
    @Transactional
    @WithAccount(username = "scene")
    void onAsyncMessagingException_notRollback() throws Exception {
        Mockito.doThrow(AsyncMessagingException.class).when(mailSender).onApplicationEvent(any(MemberQuitEvent.class));

        Account manager = accountFactory.create("magazineManager", "manager@email.com", "password");
        Magazine managed = magazineFactory.create(manager.getUsername(), manager.getEmail(), "m1", "short", "long", "MANAGED");
        managed.addMember(manager.getUsername(), "scene");
        if (!managed.getMembers().contains("scene")) fail("Illegal Test State");

        mockMvc.perform(
                delete("/magazines/" + managed.getId() + "/members")
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/magazines/" + managed.getId()))
        ;
        List<String> members = managed.getMembers();
        assertThat(members).doesNotContain("scene");
    }

}
