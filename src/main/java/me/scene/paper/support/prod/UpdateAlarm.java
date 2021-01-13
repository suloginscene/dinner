package me.scene.paper.support.prod;

import lombok.RequiredArgsConstructor;
import me.scene.paper.common.communication.mail.message.MailMessage;
import me.scene.paper.common.communication.mail.sender.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Profile({"prod1", "prod2"})
@Component
@RequiredArgsConstructor
public class UpdateAlarm {

    @Value("${app.developer}")
    private String developer;

    private final MailSender mail;


    @PostConstruct
    void sendToManager() {
        MailMessage message = new MailMessage("[페이퍼] 업데이트가 완료되었습니다.", developer, "업데이트가 완료되었습니다.");
        mail.send(message);
    }

}
