package me.scene.dinner.mail.infra;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.account.domain.account.TempPasswordIssuedEvent;
import me.scene.dinner.account.domain.tempaccount.TempAccountCreatedEvent;
import me.scene.dinner.board.magazine.domain.event.MemberAppliedEvent;
import me.scene.dinner.board.magazine.domain.event.MemberManagedEvent;
import me.scene.dinner.board.magazine.domain.event.MemberQuitEvent;
import me.scene.dinner.mail.service.AsyncMessagingException;
import me.scene.dinner.mail.service.SyncMessagingException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Slf4j
@Component
public class TestMailSender extends ConsoleMailSender {

    private Thread lastThread;

    @Override
    protected void send(String subject, String to, String text) {
        super.send(subject, to, text);
        lastThread = Thread.currentThread();
    }

    public Thread lastThread() {
        return lastThread;
    }

    public void sync() {
    }

    @Override
    public void onApplicationEvent(TempAccountCreatedEvent event) throws SyncMessagingException {
        super.onApplicationEvent(event);
        sync();
    }

    @Override
    public void onApplicationEvent(TempPasswordIssuedEvent event) throws SyncMessagingException {
        super.onApplicationEvent(event);
        sync();
    }

    @Override
    public void onApplicationEvent(MemberAppliedEvent event) throws SyncMessagingException {
        super.onApplicationEvent(event);
        sync();
    }

    public void async() {
    }

    @Override
    public void onApplicationEvent(MemberQuitEvent event) throws AsyncMessagingException {
        super.onApplicationEvent(event);
        async();
    }

    @Override
    public void onApplicationEvent(MemberManagedEvent event) throws AsyncMessagingException {
        super.onApplicationEvent(event);
        async();
    }

}
