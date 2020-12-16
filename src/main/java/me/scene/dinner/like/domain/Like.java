package me.scene.dinner.like.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@Entity @Table(name = "likes")
@Getter @NoArgsConstructor(access = PROTECTED)
public class Like extends BaseEntity {

    private String username;
    private Long articleId;
    private String articleWriter;
    private String articleTitle;

    public Like(String username, Long articleId, String articleWriter, String articleTitle) {
        this.username = username;
        this.articleId = articleId;
        this.articleWriter = articleWriter;
        this.articleTitle = articleTitle;
    }

    public LikedEvent createEvent() {
        return new LikedEvent(username, articleId, articleWriter, articleTitle);
    }

}
