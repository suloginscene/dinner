package me.scene.dinner.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
public class StatusApiController {

    private final String profile;
    private final LocalDateTime startedAt;

    @Autowired
    public StatusApiController(Environment environment) {
        this.profile = environment.getActiveProfiles()[0];
        this.startedAt = LocalDateTime.now();
    }


    @GetMapping("/api/status/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/api/status/profile")
    public String profile() {
        return profile;
    }

    @GetMapping("/api/status/version")
    public String startedAt() {
        return startedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
