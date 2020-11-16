package me.scene.dinner.board.reply.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.common.exception.NotOwnerException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

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

    public void confirmWriter(String current) {
        if (!current.equals(writer)) throw new NotOwnerException(current);
    }

}
