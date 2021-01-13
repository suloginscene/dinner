package me.scene.paper.service.account.domain.account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.common.framework.entity.BaseEntity;
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

    @Column(length = 16, nullable = false)
    private String receiver;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean checked;

    @CreatedDate @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public Notification(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
        this.checked = false;
    }

    protected void check() {
        checked = true;
    }

}
