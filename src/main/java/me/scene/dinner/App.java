package me.scene.dinner;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAsync @EnableScheduling @EnableJpaAuditing
public class App {

    public static final String SPRING_CONFIGS = "spring.config.location="
            + "classpath:application.yml,"
            + "file:/paper/conf/app.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class)
                .properties(SPRING_CONFIGS)
                .run(args);
    }

}
