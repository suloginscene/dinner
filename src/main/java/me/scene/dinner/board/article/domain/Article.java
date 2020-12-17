package me.scene.dinner.board.article.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Board;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.common.Point;
import me.scene.dinner.board.topic.domain.Topic;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Article extends Board {

    private String title;

    @Lob
    private String content;

    private boolean publicized;

    private int read;
    private int likes;

    @ManyToOne(fetch = LAZY)
    private Topic topic;

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "article_id")
    private final List<Reply> replies = new ArrayList<>();


    public Article(Topic topic, String owner, String title, String content, boolean publicized) {
        topic.getMagazine().checkAuthorization(owner);
        topic.addArticle();
        this.owner = new Owner(owner);
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.publicized = publicized;
        logWriter();
    }


    public void update(String current, String title, String content, boolean publicized) {
        owner.identify(current);
        this.title = title;
        this.content = content;
        this.publicized = publicized;
        logWriter();
    }

    public void beforeDelete(String current) {
        owner.identify(current);
        this.publicized = false;
        logWriter();
        topic.removeArticle();
    }

    private void logWriter() {
        if (publicized) topic.getMagazine().logWriting(owner.getOwnerName());
        else topic.getMagazine().logErasing(owner.getOwnerName());
    }


    public void read() {
        read++;
        rate(Point.READ);
    }

    public void like() {
        likes++;
        rate(Point.LIKE);
    }

    public void dislike() {
        if (likes < 1) return;
        likes--;
        rate(-Point.LIKE);
    }

    public void add(Reply reply) {
        replies.add(reply);
    }

    public void remove(Reply reply) {
        replies.remove(reply);
    }

    public Optional<Reply> findReplyById(Long replyId) {
        return replies.stream().filter(r -> r.getId().equals(replyId)).findAny();
    }

    @Override
    public void rate(int point) {
        super.rate(point);
        topic.rate(point);
    }

}
