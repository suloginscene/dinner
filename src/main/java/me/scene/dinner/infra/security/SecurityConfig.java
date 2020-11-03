package me.scene.dinner.infra.security;

import me.scene.dinner.domain.MainController;
import me.scene.dinner.domain.account.application.AccountService;
import me.scene.dinner.domain.tag.ui.TagController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final PersistentTokenRepository tokenRepository;

    @Autowired
    public SecurityConfig(AccountService accountService, PersistentTokenRepository tokenRepository) {
        this.accountService = accountService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .mvcMatchers(HttpMethod.GET,
                        MainController.URL_HOME, MainController.URL_ABOUT,
                        "/signup", "/verify", "/login", "/forgot", "/sent", "/@*",
                        "/magazines/*", "/topics/*", "/articles/*",
                        TagController.URL, TagController.URL + "/*"
                ).permitAll()

                .mvcMatchers(HttpMethod.POST,
                        "/signup", "/forgot"
                )
                .permitAll()

                .anyRequest().authenticated()
        ;


        http.formLogin()
                .loginPage("/login");

        http.logout()
                .logoutSuccessUrl(MainController.URL_HOME);

        http.rememberMe()
                .userDetailsService(accountService)
                .tokenRepository(tokenRepository);

    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .mvcMatchers("/fonts/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
