package me.scene.dinner.infra.config;


import me.scene.dinner.MainController;
import me.scene.dinner.domain.account.AccountController;
import me.scene.dinner.domain.article.ArticleController;
import me.scene.dinner.domain.magazine.MagazineController;
import me.scene.dinner.domain.topic.TopicController;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .mvcMatchers(HttpMethod.GET,
                        MainController.URL_HOME, MainController.URL_ABOUT,
                        AccountController.URL_SIGNUP, AccountController.URL_LOGIN,
                        ArticleController.URL + "/*", TopicController.URL + "/*", MagazineController.URL + "/*"
                ).permitAll()

//                .mvcMatchers(HttpMethod.POST,
//                        "")
//                .permitAll()

                .anyRequest().authenticated()
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .mvcMatchers("/fonts/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
