package me.scene.dinner.infra.mail;

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
public class HtmlMailSender implements MailSender {

    private final JavaMailSender javaMailSender;

    @Autowired
    public HtmlMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(MailMessage mailMessage) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setSubject(mailMessage.getSubject());
        mimeMessageHelper.setTo(mailMessage.getTo());
        mimeMessageHelper.setText(mailMessage.getText());

        javaMailSender.send(mimeMessage);
    }

}
