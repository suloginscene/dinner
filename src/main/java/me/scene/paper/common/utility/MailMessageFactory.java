package me.scene.paper.common.utility;

import lombok.RequiredArgsConstructor;
import me.scene.paper.common.mail.message.MailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Component
@RequiredArgsConstructor
public class MailMessageFactory {

    @Value("${app.url}")
    private String url;

    private final TemplateEngine templateEngine;


    public MailMessage verification(String email, String token) {
        String subject = "[페이퍼] 계정 인증 메일입니다.";

        Context context = new Context();
        context.setVariable("host", url);
        context.setVariable("link", "/verify?email=" + email + "&token=" + token);

        String message = templateEngine.process("mail/verification", context);

        return new MailMessage(subject, email, message);
    }

    public MailMessage randomPassword(String email, String rawPassword) {
        String subject = "[페이퍼] 임의의 비밀번호가 적용되었습니다.";

        Context context = new Context();
        context.setVariable("password", rawPassword);

        String message = templateEngine.process("mail/randomPassword", context);

        return new MailMessage(subject, email, message);
    }

}
