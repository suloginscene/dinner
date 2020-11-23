package me.scene.dinner.mail.infra;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.mail.service.MailSender;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"local", "test"})
@Slf4j
@Component
public class ConsoleMailSender extends MailSender {

    private static final StringBuilder sb = new StringBuilder();

    @Override
    public void send(String subject, String to, String text) {
        sb.setLength(0);
        sb.append("\n")
                .append("\t").append("subject: ").append(subject).append("\n")
                .append("\t").append("to     : ").append(to).append("\n")
                .append("\t").append("text   : ").append(text).append("\n").append("\n");
        log.info(sb.toString());
    }

}
