package me.scene.dinner.infra.config;


import me.scene.dinner.MainController;
import me.scene.dinner.domain.account.AccountController;
import me.scene.dinner.domain.board.article.ArticleController;
import me.scene.dinner.domain.board.magazine.MagazineController;
import me.scene.dinner.domain.board.topic.TopicController;
import me.scene.dinner.domain.tag.TagController;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .mvcMatchers(HttpMethod.GET,
                        MainController.URL_HOME, MainController.URL_ABOUT,
                        AccountController.URL_SIGNUP, AccountController.URL_VERIFY, AccountController.URL_LOGIN,
                        MagazineController.FORM, TopicController.FORM, ArticleController.FORM,
                        MagazineController.URL + "/*", TopicController.URL + "/*", ArticleController.URL + "/*",
                        TagController.URL, TagController.URL + "/*"
                ).permitAll()

                .mvcMatchers(HttpMethod.POST,
                        AccountController.URL_SIGNUP)
                .permitAll()

                .anyRequest().authenticated()
        ;


        http.formLogin()
                .loginPage(AccountController.URL_LOGIN);

        http.logout()
                .logoutSuccessUrl(MainController.URL_HOME);

    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .mvcMatchers("/fonts/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
