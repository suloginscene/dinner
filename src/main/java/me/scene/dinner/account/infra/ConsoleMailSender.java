package me.scene.dinner.account.infra;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.account.application.MailSender;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"local", "test"})
@Slf4j
@Component
public class ConsoleMailSender extends MailSender {

    @Override
    public void send(String subject, String to, String text) {
        log.info("[" + subject + "] " + to + ", " + text);
    }

}
