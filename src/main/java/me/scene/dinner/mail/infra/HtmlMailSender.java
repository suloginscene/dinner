package me.scene.dinner.mail.infra;

import me.scene.dinner.mail.MailSender;
import me.scene.dinner.mail.exception.RuntimeMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Profile({"dev", "prod"})
@Component
public class HtmlMailSender extends MailSender {

    private final JavaMailSender javaMailSender;

    @Autowired
    public HtmlMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(String subject, String to, String text) throws RuntimeMessagingException {
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setText(text);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeMessagingException();
        }
    }

}
