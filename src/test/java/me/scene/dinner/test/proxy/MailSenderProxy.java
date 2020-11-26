package me.scene.dinner.test.proxy;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.mail.infra.ConsoleMailSender;
import me.scene.dinner.mail.service.MailSender;

@Slf4j
public class MailSenderProxy extends MailSender {

    private final ConsoleMailSender consoleMailSender = new ConsoleMailSender();

    private static final String BANNER = "\n" + "-".repeat(35) + " P R O X Y " + "-".repeat(34);

    private Thread lastThread;

    @Override
    protected void send(String subject, String to, String text) {
        log.info(BANNER);
        consoleMailSender.send(subject, to, text);
        lastThread = Thread.currentThread();
    }

    public Thread lastThread() {
        return lastThread;
    }

}
