package me.scene.dinner.utils.authentication;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.utils.factory.AccountFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountFactory accountFactory;
    private final AccountService accountService;

    @Autowired
    public WithAccountSecurityContextFactory(AccountFactory accountFactory, AccountService accountService) {
        this.accountFactory = accountFactory;
        this.accountService = accountService;
    }

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        String username = withAccount.username();
        accountFactory.create(username, username + "@email.com", "password");

        UserDetails principal = accountService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

}
