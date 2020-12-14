package me.scene.dinner.board.domain.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.domain.common.NotOwnerException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Reply {

    @Id @GeneratedValue
    private Long id;

    private String writer;

    private String content;

    private LocalDateTime createdAt;


    protected Reply() {
    }

    public static Reply create(String writer, String content) {
        Reply reply = new Reply();
        reply.writer = writer;
        reply.content = content;
        reply.createdAt = LocalDateTime.now();
        return reply;
    }

    public void confirmWriter(String current) {
        if (current.equals(writer)) return;
        throw new NotOwnerException(current);
    }

}
