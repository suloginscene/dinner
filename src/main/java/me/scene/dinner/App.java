package me.scene.dinner;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class App {

    public static final String SPRING_CONFIGS = "spring.config.location="
            + "classpath:application.yml,"
            + "file:/dinner-ref/app/config/file-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class)
                .properties(SPRING_CONFIGS)
                .run(args);
    }

}
