package me.scene.dinner.infra.config.security;

import me.scene.dinner.MainController;
import me.scene.dinner.domain.account.AccountController;
import me.scene.dinner.domain.account.AccountService;
import me.scene.dinner.domain.board.UrlUtils;
import me.scene.dinner.domain.tag.TagController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final PersistentTokenRepository tokenRepository;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public SecurityConfig(AccountService accountService, PersistentTokenRepository tokenRepository, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.accountService = accountService;
        this.tokenRepository = tokenRepository;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .mvcMatchers(HttpMethod.GET,
                        MainController.URL_HOME, MainController.URL_ABOUT,
                        AccountController.URL_SIGNUP, AccountController.URL_VERIFY, AccountController.URL_LOGIN,
                        AccountController.URL_FORGOT, AccountController.URL_PROFILE + "/*",
                        UrlUtils.read("*"), UrlUtils.read("*", "*"), UrlUtils.read("*", "*", "*"),
                        TagController.URL, TagController.URL + "/*"
                ).permitAll()

                .mvcMatchers(HttpMethod.POST,
                        AccountController.URL_SIGNUP, AccountController.URL_FORGOT, AccountController.URL_PROFILE + "/*"
                )
                .permitAll()

                .anyRequest().authenticated()
        ;


        http.formLogin()
                .loginPage(AccountController.URL_LOGIN)
                .successHandler(authenticationSuccessHandler);

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
