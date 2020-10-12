package me.scene.dinner.infra.config.url;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class UrlConfig {

    @Configuration
    @Profile({"local", "test"})
    static class LocalUrl {
        @Bean
        URL url() {
            return new URL("http://localhost:8080");
        }
    }

    @Configuration
    @Profile("dev")
    static class DevUrl {
        @Bean
        URL url() {
            return new URL("http://scene-cho.cf");
        }
    }

    @Configuration
    @Profile("prod")
    static class ProdUrl {
        @Bean
        URL url() {
            return new URL("");
        }
    }

}

