package me.scene.dinner.mail.ui;

import me.scene.dinner.account.domain.account.AccountRepository;
import me.scene.dinner.utils.authentication.WithAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class MailControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired AccountRepository accountRepository;

    @AfterEach
    void clear() {
        accountRepository.deleteAll();
    }


    @Test
    void sentToAccount_hasEmail() throws Exception {
        mockMvc.perform(
                get("/sent-to-account?email=test@email.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/mail/account"))
                .andExpect(model().attributeExists("email"))
        ;
    }

    @Test
    @WithAccount(username = "scene")
    void sentToManager_returnPage() throws Exception {
        mockMvc.perform(
                get("/sent-to-manager")
                        .param("magazineId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/mail/manager"))
        ;
    }

}
