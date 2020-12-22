package me.scene.dinner.common.mail.service.event;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.common.mail.service.sender.MailMessage;
import me.scene.dinner.common.mail.service.sender.MailSender;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MailEventListener {

    private final MailSender sender;


    @Async
    @EventListener
    public void sendByEvent(MailEvent event) {
        MailMessage mailMessage = event.getMailMessage();
        sender.send(mailMessage);
    }

}
