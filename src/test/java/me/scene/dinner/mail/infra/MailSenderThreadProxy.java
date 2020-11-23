package me.scene.dinner.mail.infra;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.mail.service.MailSender;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"test-mailSender"})
@Slf4j
@Component
public class MailSenderThreadProxy extends MailSender {

    private Thread lastThread;
    private final ConsoleMailSender consoleMailSender = new ConsoleMailSender();

    @Override
    protected void send(String subject, String to, String text) {
        log.info("-".repeat(35) + " P R O X Y " + "-".repeat(34));
        consoleMailSender.send(subject, to, text);

        lastThread = Thread.currentThread();
    }

    public Thread lastThread() {
        return lastThread;
    }

}
