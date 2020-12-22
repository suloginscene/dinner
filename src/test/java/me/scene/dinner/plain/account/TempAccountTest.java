package me.scene.dinner.plain.account;

import me.scene.dinner.account.domain.tempaccount.model.TempAccount;
import me.scene.dinner.account.domain.tempaccount.exception.VerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Account(temp)")
class TempAccountTest {

    TempAccount tempAccount;

    @BeforeEach
    void create() {
        tempAccount = new TempAccount("username", "email@email.com", "encodedPassword");
    }


    @Nested class OnCreate {
        @Test
        void hasToken() {
            assertNotNull(tempAccount.getVerificationToken());
        }
    }

    @Nested class OnVerify {
        @Test
        void doesNotThrow() {
            assertDoesNotThrow(
                    () -> tempAccount.verify(tempAccount.getVerificationToken())
            );
        }

        @Nested class WithInValidToken {
            @Test
            void throwsException() {
                assertThrows(
                        VerificationException.class,
                        () -> tempAccount.verify("invalid")
                );
            }
        }
    }

}
