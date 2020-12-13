package me.scene.dinner.like;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @EqualsAndHashCode(of = "id", callSuper = false)
public class Likes extends AbstractAggregateRoot<Likes> {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private Long articleId;

    protected Likes() {
    }

    public static Likes create(String username, Long articleId, String title, String writer) {
        Likes likes = new Likes();
        likes.username = username;
        likes.articleId = articleId;
        likes.registerEvent(new LikedEvent(writer, username, title));
        return likes;
    }

}
