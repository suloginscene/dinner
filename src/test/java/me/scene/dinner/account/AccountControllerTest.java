//package me.scene.dinner.account;
//
//import me.scene.dinner.account.domain.Account;
//import me.scene.dinner.account.domain.AccountRepository;
//import me.scene.dinner.account.domain.TempAccount;
//import me.scene.dinner.account.domain.TempAccountRepository;
//import me.scene.dinner.account.utils.WithAccount;
//import me.scene.dinner.account.application.MailSender;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@Transactional
//@SpringBootTest
//@AutoConfigureMockMvc
//class AccountControllerTest {
//
//    @Autowired MockMvc mockMvc;
//    @Autowired TempAccountRepository tempAccountRepository;
//    @Autowired AccountRepository accountRepository;
//    @Autowired PasswordEncoder passwordEncoder;
//    @MockBean MailSender mailSender;
//
//    @Test
//    void profilePage_nonExistent_handleException() throws Exception {
//        mockMvc.perform(
//                get("/@scene")
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("page/error/user_not_found"))
//        ;
//    }
//
//    @Test
//    void profilePage_notOwner_readOnly() throws Exception {
//        TempAccount tempAccount = TempAccount.create("scene", "scene@email.com", "password", passwordEncoder);
//        Account account = Account.create(tempAccount);
//        accountRepository.save(account);
//
//        mockMvc.perform(
//                get("/@scene")
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("page/account/profile"))
//                .andExpect(model().attribute("isOwner", false))
//        ;
//    }
//
//    @Test
//    @WithAccount(username = "scene")
//    void profilePage_owner_modifiable() throws Exception {
//        mockMvc.perform(
//                get("/@scene")
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("page/account/profile"))
//                .andExpect(model().attribute("isOwner", true))
//        ;
//    }
//
//    // TODO temporary version
//    @Test
//    @WithAccount(username = "scene")
//    void changePassword_changed() throws Exception {
//        Account scene = accountRepository.findByUsername("scene").orElseThrow();
//        String encodedOldPassword = scene.getPassword();
//
//        mockMvc.perform(
//                post("/@scene")
//                        .with(csrf())
//                        .param("password", "newPassword")
//        )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/@scene?success"))
//        ;
//
//        String encodedNewPassword = scene.getPassword();
//        assertThat(encodedNewPassword).startsWith("{bcrypt}");
//        assertThat(encodedNewPassword).isNotEqualTo(encodedOldPassword);
//    }
//
//    // TODO temporary version
//    @Test
//    @WithAccount(username = "scene")
//    void changePassword_short_unchanged() throws Exception {
//        Account scene = accountRepository.findByUsername("scene").orElseThrow();
//        String encodedOldPassword = scene.getPassword();
//
//        mockMvc.perform(
//                post("/@scene")
//                        .with(csrf())
//                        .param("password", "short")
//        )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/@scene?short"))
//        ;
//
//        String encodedNewPassword = scene.getPassword();
//        assertThat(encodedNewPassword).isEqualTo(encodedOldPassword);
//    }
//
//    // TODO temporary version
//    @Test
//    @WithAccount(username = "another")
//    void changePassword_notOwner_handleException() throws Exception {
//        TempAccount tempAccount = TempAccount.create("scene", "scene@email.com", "password", passwordEncoder);
//        Account account = Account.create(tempAccount);
//        accountRepository.save(account);
//
//        Account scene = accountRepository.findByUsername("scene").orElseThrow();
//        String encodedOldPassword = scene.getPassword();
//
//        mockMvc.perform(
//                post("/@scene")
//                        .with(csrf())
//                        .param("password", "newPassword")
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("page/error/forbidden"))
//        ;
//
//        String encodedNewPassword = scene.getPassword();
//        assertThat(encodedNewPassword).isEqualTo(encodedOldPassword);
//    }
//
//}
