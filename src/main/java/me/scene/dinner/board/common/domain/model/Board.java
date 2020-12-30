package me.scene.dinner.board.common.domain.model;

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

    @Column(length = 20, nullable = false)
    protected String title;

    @Embedded
    protected Owner owner;

    @Embedded
    protected Point point = new Point();

    @CreatedDate @Column(updatable = false, nullable = false)
    protected LocalDateTime createdAt;


    public final void rate(int p) {
        point.add(p);
        propagateRate(p);
    }

    abstract protected void propagateRate(int point);

}
