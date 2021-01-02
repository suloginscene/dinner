package me.scene.dinner.support;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.common.mail.message.MailMessage;
import me.scene.dinner.common.mail.sender.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class UpdateAlarm {

    @Value("${app.developer}")
    private String developer;

    private final String profile;
    private final MailSender mail;

    private static final Set<String> conditions = Set.of("prod1", "prod2");


    @Autowired
    public UpdateAlarm(Environment environment, MailSender mailSender) {
        this.profile = environment.getActiveProfiles()[0];
        this.mail = mailSender;
    }

    @PostConstruct
    void sendToManager() {
        if (conditions.contains(profile)) {
            MailMessage message = new MailMessage("[페이퍼] 업데이트가 완료되었습니다.", developer, "업데이트가 완료되었습니다.");
            mail.send(message);
        }
    }

}
