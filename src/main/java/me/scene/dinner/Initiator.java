package me.scene.dinner;

import me.scene.dinner.account.application.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Initiator implements ApplicationRunner {

    private final Environment environment;
    private final AccountService accountService;

    @Autowired
    public Initiator(Environment environment, AccountService accountService) {
        this.environment = environment;
        this.accountService = accountService;
    }

    @Override
    public void run(ApplicationArguments args) {
        String activeProfile = environment.getActiveProfiles()[0];
        if (!activeProfile.equals("local")) return;

        registerInitialUser();
    }

    private void registerInitialUser() {
        accountService.saveTemp("test", "test@email.com", "testPassword");
        accountService.transferFromTempToRegular("test@email.com");
    }

}
