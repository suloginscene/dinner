package me.scene.dinner.board.magazine.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.utils.factory.AccountFactory;
import me.scene.dinner.utils.factory.MagazineFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MagazineRestControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    @Autowired MagazineFactory magazineFactory;


    @Test
    void showList_hasMagazines() throws Exception {
        Account account = accountFactory.create("scene", "scene@email.com", "password");
        magazineFactory.create(account.getUsername(), "m1", "short", "long", "OPEN");
        magazineFactory.create(account.getUsername(), "m2", "short", "long", "OPEN");
        magazineFactory.create(account.getUsername(), "m3", "short", "long", "OPEN");

        mockMvc.perform(
                get("/api/magazines")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("[0]").exists())
                .andExpect(jsonPath("[1]").exists())
                .andExpect(jsonPath("[2]").exists())
                .andExpect(jsonPath("[3]").doesNotExist())
                .andExpect(jsonPath("[0].*", hasSize(2)))
                .andExpect(jsonPath("[0].id").exists())
                .andExpect(jsonPath("[0].title").exists())
                .andExpect(jsonPath("[0].manager").doesNotExist())
        ;
    }

}