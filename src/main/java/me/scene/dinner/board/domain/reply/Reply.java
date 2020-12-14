package me.scene.dinner.board.domain.reply;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.domain.article.Article;
import me.scene.dinner.board.domain.common.NotOwnerException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

// TODO
@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Reply {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Article article;

    private String writer;


    private String content;

    private LocalDateTime createdAt;


    protected Reply() {
    }

    public static Reply create(Article article, String writer, String content) {
        Reply reply = new Reply();
        article.add(reply);
        reply.article = article;
        reply.writer = writer;
        reply.content = content;
        reply.createdAt = LocalDateTime.now();
        return reply;
    }

    public void beforeDelete(String current) {
        confirmWriter(current);
        article.remove(this);
    }


    private void confirmWriter(String current) {
        if (current.equals(writer)) return;
        throw new NotOwnerException(current);
    }

}
