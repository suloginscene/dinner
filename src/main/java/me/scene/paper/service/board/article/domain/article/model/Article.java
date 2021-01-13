package me.scene.paper.service.board.article.domain.article.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.service.board.common.domain.model.Board;
import me.scene.paper.service.board.common.domain.model.Point;
import me.scene.paper.service.board.topic.domain.model.Topic;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Article extends Board {

    @Lob
    private String content;

    private boolean publicized;

    private int read;


    @ManyToOne(fetch = LAZY)
    private Topic topic;

    @BatchSize(size = 100)
    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "article_id")
    private final List<Reply> replies = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "article_id")
    private final Set<Like> likes = new HashSet<>();

    @BatchSize(size = 100)
    @OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "article")
    private final Set<ArticleTag> articleTags = new HashSet<>();


    public Article(Topic topic, String owner, String title, String content, boolean publicized) {
        super(title, owner);
        topic.propagateAuthorize(owner);
        this.content = content;
        this.publicized = publicized;
        this.topic = topic;
        topic.addArticle();
        topic.propagateLogWriter(owner, publicized);
    }


    public void update(String current, String title, String content, boolean publicized) {
        owner.identify(current);
        this.title = title;
        this.content = content;
        this.publicized = publicized;
        topic.propagateLogWriter(current, publicized);
    }

    public void beforeDelete(String current) {
        owner.identify(current);
        rate(-point.get());
        topic.removeArticle();
        topic.propagateLogWriter(current, false);
    }


    public void addReply(Reply reply) {
        replies.add(reply);
    }

    public void removeReply(String username, Long replyId) {
        Optional<Reply> optionalReply = replies.stream()
                .filter(reply -> reply.getId().equals(replyId))
                .findAny();

        optionalReply.ifPresent(reply -> {
            reply.getOwner().identify(username);
            replies.remove(reply);
        });
    }


    public void read(String username) {
        if (publicized) {
            read++;
            rate(Point.READ);
        } else {
            owner.identify(username);
        }
    }

    public void like(String username) {
        Like like = new Like(username);
        if (likes.contains(like)) return;

        likes.add(like);
        rate(Point.LIKE);
    }

    public void dislike(String username) {
        Like like = new Like(username);
        if (!likes.contains(like)) return;

        likes.remove(like);
        rate(-Point.LIKE);
    }

    public boolean isLikedBy(String username) {
        return likes.contains(new Like(username));
    }

    @Override
    protected void propagateRate(int point) {
        topic.rate(point);
    }

}
