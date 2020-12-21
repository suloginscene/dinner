package me.scene.dinner.plain;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.Profile;
import me.scene.dinner.account.domain.account.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Account")
class AccountTest {

    Account account;

    @BeforeEach
    void create() {
        account = new Account("username", "email", "encodedPassword");
    }


    @Nested class OnCreate {
        @Test
        void isUser() {
            Role role = account.getRole();
            assertThat(role).isEqualTo(Role.USER);
        }

        @Test
        void hasEmptyProfile() {
            Profile profile = account.getProfile();
            assertThat(profile).isNotNull();
        }
    }

}
