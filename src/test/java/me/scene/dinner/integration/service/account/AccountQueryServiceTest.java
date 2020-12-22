package me.scene.dinner.integration.service.account;

import me.scene.dinner.account.application.query.AccountQueryService;
import me.scene.dinner.account.application.query.dto.UserAccount;
import me.scene.dinner.integration.utils.AccountTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("Account_query")
class AccountQueryServiceTest {

    @Autowired AccountQueryService query;

    @Autowired AccountTestHelper helper;


    @BeforeEach
    void createAccount() {
        helper.createAccount("username", "email@email.com", "password");
    }

    @AfterEach
    void clear() {
        helper.clearAccounts();
    }


    @Nested class OnLoad {
        @Test
        void returns_userAccount() {
            UserDetails userDetails = query.loadUserByUsername("username");
            assertThat(userDetails).isInstanceOf(UserAccount.class);
        }
    }

}
