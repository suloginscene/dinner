package me.scene.dinner.notification.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.common.entity.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter @EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
public class Notification extends BaseEntity {

    private String receiver;

    private String message;

    private boolean checked;

    @CreatedDate @Column(updatable = false)
    private LocalDateTime createdAt;

    public Notification(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
        this.checked = false;
    }

    public void check() {
        checked = true;
    }

}
