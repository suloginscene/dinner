package me.scene.dinner.like;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Likes {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private Long articleId;

    protected Likes() {
    }

    public static Likes create(String username, Long articleId) {
        Likes likes = new Likes();
        likes.username = username;
        likes.articleId = articleId;
        return likes;
    }

}
