package me.scene.dinner.account.ui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class MailControllerTest {

    @Autowired MockMvc mockMvc;

    @Test
    void sent_hasEmail() throws Exception {
        mockMvc.perform(
                get("/sent?email=test@email.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/main/sent"))
                .andExpect(model().attributeExists("email"))
        ;
    }

}
