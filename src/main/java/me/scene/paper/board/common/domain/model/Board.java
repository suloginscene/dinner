package me.scene.paper.board.common.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.common.entity.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;


@MappedSuperclass
@Getter @EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
public abstract class Board extends BaseEntity {

    @Column(length = 20, nullable = false)
    protected String title;

    @CreatedDate @Column(updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    @Embedded
    protected Owner owner;

    @Embedded
    protected Point point = new Point();


    protected Board(String title, String owner) {
        this.title = title;
        this.owner = new Owner(owner);
    }


    public final void rate(int p) {
        point.add(p);
        propagateRate(p);
    }

    abstract protected void propagateRate(int point);

}
