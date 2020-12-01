package me.scene.dinner.board.article.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.reply.domain.Reply;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.common.exception.NotOwnerException;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @EqualsAndHashCode(of = "id", callSuper = false)
public class Article extends AbstractAggregateRoot<Article> {

    @Id @GeneratedValue
    private Long id;

    @JsonIgnore
    private String writer;


    private String title;

    @Lob @JsonIgnore
    private String content;

    @Enumerated(EnumType.STRING) @JsonIgnore
    private Status status;

    private LocalDateTime createdAt;


    private int read;

    private int likes;


    @ManyToOne(fetch = LAZY) @JsonIgnore
    private Topic topic;

    @OneToMany(mappedBy = "article", orphanRemoval = true) @JsonIgnore
    private final List<Reply> replies = new ArrayList<>();

    @JsonIgnore
    public boolean isPublic() {
        return status == Status.PUBLIC;
    }


    protected Article() {
    }

    public static Article create(Topic topic, String writer, String title, String content, String status) {
        topic.getMagazine().checkAuthorization(writer);
        Article article = new Article();
        article.writer = writer;
        article.title = title;
        article.content = content;
        article.createdAt = LocalDateTime.now();
        article.topic = topic;
        article.status = Status.valueOf(status);
        topic.add(article);
        article.toggleWriterRegistration();
        return article;
    }

    public void update(String current, String title, String content, String status) {
        confirmWriter(current);
        this.title = title;
        this.content = content;
        this.status = Status.valueOf(status);
        toggleWriterRegistration();
    }

    public void read() {
        read++;
    }

    public void like() {
        likes++;
    }

    public void dislike() {
        if (likes < 1) return;
        likes--;
    }

    public void beforeDelete(String current) {
        confirmWriter(current);
        topic.getMagazine().removeWriter(writer);
        topic.remove(this);
    }

    private void toggleWriterRegistration() {
        if (status == Status.PUBLIC) {
            topic.getMagazine().addWriter(writer);
        } else {
            topic.getMagazine().removeWriter(writer);
        }
    }


    public void confirmWriter(String current) {
        if (current.equals(writer)) return;
        throw new NotOwnerException(current);
    }


    public void add(Reply reply) {
        replies.add(reply);
    }

    public void remove(Reply reply) {
        replies.remove(reply);
    }

}
