package me.scene.dinner.infra.config.security;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.MainController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Configuration
public class AuthenticationSuccessHandlerConfig {

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler(MainController.URL_HOME) {
            @Override
            protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
                HttpSession session = request.getSession();

                String redirectTo = (String) session.getAttribute("redirectTo");
                if (redirectTo == null) return super.determineTargetUrl(request, response);

                session.removeAttribute("redirectTo");
                log.debug("Redirect to: {}", redirectTo);
                return redirectTo;
            }
        };
    }

}
