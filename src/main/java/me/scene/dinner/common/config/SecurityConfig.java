package me.scene.dinner.common.config;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.common.security.LoginSuccessHandler;
import me.scene.dinner.tag.TagController;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final PersistentTokenRepository tokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET,
                        "/", "/about", "/magazines",
                        "/signup", "/verify", "/login", "/forgot", "/sent-to-account", "/@*",
                        "/magazines/*", "/topics/*", "/articles/*",
                        TagController.URL, TagController.URL + "/*",
                        "/api/magazines", "/api/magazines/*", "/api/articles/*"
                ).permitAll()
                .mvcMatchers(HttpMethod.POST,
                        "/signup", "/forgot"
                ).permitAll()
                .anyRequest().authenticated()
        ;

        http.formLogin()
                .loginPage("/login")
                .successHandler(new LoginSuccessHandler("/"))
        ;

        http.logout()
                .logoutSuccessUrl("/")
        ;

        http.rememberMe()
                .userDetailsService(accountService)
                .tokenRepository(tokenRepository)
        ;

    }

    @Override
    public void configure(WebSecurity web) {

        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .mvcMatchers("/fonts/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        ;

    }

}
