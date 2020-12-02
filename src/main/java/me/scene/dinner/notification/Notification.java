package me.scene.dinner.notification;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Notification {

    @Id @GeneratedValue
    private Long id;

    private String receiver;

    private String message;

    private boolean checked;

    private LocalDateTime createdAt;

    protected Notification() {
    }

    public static Notification create(String receiver, String message) {
        Notification notification = new Notification();
        notification.receiver = receiver;
        notification.message = message;
        notification.checked = false;
        notification.createdAt = LocalDateTime.now();
        return notification;
    }

    public void check() {
        checked = true;
    }

}
