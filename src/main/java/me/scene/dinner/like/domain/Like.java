package me.scene.dinner.like.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;


@Entity @Table(name = "likes")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Like extends BaseEntity {

    private String username;

    @ManyToOne(fetch = LAZY)
    private Article article;


    private Like(String username, Article article) {
        this.username = username;
        this.article = article;
    }


    public static Like create(String username, Article article) {
        article.like();
        return new Like(username, article);
    }

    public void destruct() {
        article.dislike();
    }


    public LikedEvent createEvent() {
        return new LikedEvent(username, article.getId(), article.getOwner().getName(), article.getTitle());
    }

}
