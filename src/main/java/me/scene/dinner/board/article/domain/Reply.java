package me.scene.dinner.board.article.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.domain.Owner;
import me.scene.dinner.common.entity.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
public class Reply extends BaseEntity {

    @Embedded
    protected Owner owner;

    private String content;

    @CreatedDate @Column(updatable = false)
    protected LocalDateTime createdAt;


    public Reply(Owner owner, String content) {
        this.owner = owner;
        this.content = content;
    }

}
