package me.scene.paper.board.article.domain.article.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.board.common.domain.model.Owner;
import me.scene.paper.common.entity.BaseEntity;
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

    @Column(nullable = false)
    private String content;

    @CreatedDate @Column(updatable = false)
    protected LocalDateTime createdAt;


    public Reply(String owner, String content) {
        this.owner = new Owner(owner);
        this.content = content;
    }

}
