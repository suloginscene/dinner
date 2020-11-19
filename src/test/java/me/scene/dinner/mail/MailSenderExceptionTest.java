package me.scene.dinner.mail;

import me.scene.dinner.account.domain.tempaccount.TempAccount;
import me.scene.dinner.account.domain.tempaccount.TempAccountCreatedEvent;
import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
import me.scene.dinner.mail.exception.SyncMessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class MailSenderExceptionTest {

    @MockBean MailSender mailSender;

    @Autowired MockMvc mockMvc;

    @Autowired TempAccountRepository tempAccountRepository;

    @BeforeEach
    void clear() {
        tempAccountRepository.deleteAll();
    }

    @Test
    void onSyncMessagingException_rollback() throws Exception {
        doThrow(SyncMessagingException.class).when(mailSender).onApplicationEvent(any(TempAccountCreatedEvent.class));

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

    // TODO when need async mailing
    @Test
    @Disabled
    void onAsyncMessagingException_notRollback() {
    }

}
