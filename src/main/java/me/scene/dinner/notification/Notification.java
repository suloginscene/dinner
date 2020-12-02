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

    private String target;

    private String message;

    private boolean checked;

    private LocalDateTime createdAt;

    protected Notification() {
    }

    public static Notification create(String target, String message) {
        Notification notification = new Notification();
        notification.target = target;
        notification.message = message;
        notification.checked = false;
        notification.createdAt = LocalDateTime.now();
        return notification;
    }

}
