package me.scene.dinner.like.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;


@Entity @Table(name = "likes")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Like extends BaseEntity {

    private String username;

    @ManyToOne
    private Article article;


    public Like(String username, Article article) {
        this.username = username;
        this.article = article;
    }


    public LikedEvent createEvent() {
        return new LikedEvent(username, article.getId(), article.getOwner().getName(), article.getTitle());
    }

}
