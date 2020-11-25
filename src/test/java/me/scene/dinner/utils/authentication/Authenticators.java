package me.scene.dinner.utils.authentication;

import me.scene.dinner.account.application.UserAccount;
import me.scene.dinner.account.domain.account.Account;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Authenticators {

    public static void login(Account account) {
        UserDetails principal = new UserAccount(account);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities())
        );
    }

    public static void logout() {
        SecurityContextHolder.clearContext();
    }

}
