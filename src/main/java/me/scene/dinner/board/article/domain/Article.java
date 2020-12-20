package me.scene.dinner.board.article.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.domain.Board;
import me.scene.dinner.board.common.domain.Owner;
import me.scene.dinner.board.common.domain.Point;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.open.OpenMagazine;
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
import static me.scene.dinner.board.magazine.domain.common.Type.OPEN;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Article extends Board {

    @Lob
    private String content;

    private boolean publicized;

    private int read;
    private int like;

    @ManyToOne(fetch = LAZY)
    private Magazine magazine;

    @ManyToOne(fetch = LAZY)
    private Topic topic;

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "article_id")
    private final List<Reply> replies = new ArrayList<>();


    public Article(Topic topic, String owner, String title, String content, boolean publicized) {
        this.magazine = topic.getMagazine();
        magazine.authorization().check(owner);

        this.topic = topic;
        topic.getArticles().add();

        this.owner = new Owner(owner);
        this.title = title;
        this.content = content;
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

        publicized = false;
        topic.getArticles().remove();

        logWriter();
    }


    private void logWriter() {
        if (magazine.type() != OPEN) return;

        OpenMagazine open = (OpenMagazine) magazine;
        String writer = owner.getName();

        if (publicized) open.logWriting(writer);
        else open.logErasing(writer);
    }


    // rate ------------------------------------------------------------------------------------------------------------

    public void read() {
        read++;
        rate(Point.READ);
    }

    public void like() {
        like++;
        rate(Point.LIKE);
    }

    public void dislike() {
        if (like < 1) return;
        like--;
        rate(-Point.LIKE);
    }


    @Override
    protected void propagateRate(int point) {
        topic.rate(point);
    }


    // reply -----------------------------------------------------------------------------------------------------------

    public void add(Reply reply) {
        replies.add(reply);
    }

    public void remove(Long replyId, String ownerName) {

        Optional<Reply> optionalReply = findReply(replyId);
        optionalReply.ifPresent(reply -> {
            reply.getOwner().identify(ownerName);
            replies.remove(reply);
        });

    }


    private Optional<Reply> findReply(Long id) {
        return replies.stream()
                .filter(reply -> reply.getId().equals(id))
                .findAny();
    }

}
