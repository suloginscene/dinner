package me.scene.dinner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired MockMvc mockMvc;

    @Test
    void home() throws Exception {
        mockMvc.perform(
                get("/")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/main/home"))
                .andExpect(model().attribute("activeProfile", "test"))
                .andExpect(model().attributeExists("version"))
        ;
    }

    @Test
    void about() throws Exception {
        mockMvc.perform(
                get("/about")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("page/main/about"))
        ;
    }

}
