package me.scene.dinner.board.domain.common;

import lombok.Getter;
import me.scene.dinner.common.entity.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @EntityListeners(AuditingEntityListener.class)
public abstract class Board extends BaseEntity {

    @Embedded
    protected Owner owner;

    @CreatedDate @Column(updatable = false)
    protected LocalDateTime createdAt;

}
