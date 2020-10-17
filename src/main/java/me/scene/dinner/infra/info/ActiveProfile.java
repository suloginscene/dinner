package me.scene.dinner.infra.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ActiveProfile {

    private final Environment environment;

    @Autowired
    public ActiveProfile(Environment environment) {
        this.environment = environment;
    }

    public String get() {
        return environment.getActiveProfiles()[0];
    }

}
