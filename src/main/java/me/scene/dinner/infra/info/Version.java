package me.scene.dinner.infra.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Version {

    private final String version;

    @Autowired
    public Version() {
        version = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM. dd-HHmm"));
    }

    public String get() {
        return version;
    }

}
