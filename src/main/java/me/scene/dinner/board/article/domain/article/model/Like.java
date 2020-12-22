package me.scene.dinner.board.article.domain.article.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;


@Entity @Table(name = "likes")
@EqualsAndHashCode(of = "username", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
public class Like extends BaseEntity {

    private String username;

    public Like(String username) {
        this.username = username;
    }

}
