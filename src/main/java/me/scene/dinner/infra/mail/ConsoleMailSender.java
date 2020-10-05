package me.scene.dinner.infra.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"local", "test"})
@Slf4j
@Component
public class ConsoleMailSender implements MailSender {

    @Override
    public void send(MailMessage mailMessage) {
        log.info(mailMessage.toString());
    }

}
