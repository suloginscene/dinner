package me.scene.dinner.board.domain.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.domain.common.Board;
import me.scene.dinner.board.domain.common.Owner;
import me.scene.dinner.board.domain.topic.Topic;
import me.scene.dinner.tag.TaggedArticle;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.board.domain.article.RatingType.DISLIKE;
import static me.scene.dinner.board.domain.article.RatingType.LIKE;
import static me.scene.dinner.board.domain.article.RatingType.READ;

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
    private int rating;

    @OneToMany(mappedBy = "article")
    private final Set<TaggedArticle> taggedArticles = new HashSet<>();

    @ManyToOne(fetch = LAZY)
    private Topic topic;

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "article_id")
    private final List<Reply> replies = new ArrayList<>();


    public Article(Topic topic, String owner, String title, String content, boolean publicized) {
        topic.getMagazine().checkAuthorization(owner);
        topic.add(this);
        this.owner = new Owner(owner);
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.publicized = publicized;
        toggleWriterRegistration();
    }

    public void update(String current, String title, String content, boolean publicized) {
        owner.identify(current);
        this.title = title;
        this.content = content;
        this.publicized = publicized;
        toggleWriterRegistration();
    }

    public void beforeDelete(String current) {
        owner.identify(current);
        topic.getMagazine().removeWriter(owner.getOwnerName());
        topic.remove(this);
    }

    private void toggleWriterRegistration() {
        if (publicized) topic.getMagazine().addWriter(owner.getOwnerName());
        else topic.getMagazine().removeWriter(owner.getOwnerName());
    }


    private void rate(RatingType ratingType) {
        int point = ratingType.point();
        rating += point;
        topic.rate(point);
    }

    public void read() {
        read++;
        rate(READ);
    }

    public void like() {
        likes++;
        rate(LIKE);
    }

    public void dislike() {
        if (likes < 1) return;
        likes--;
        rate(DISLIKE);
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

    public TopicSummary topicSummary() {
        return new TopicSummary(topic.getId(), topic.getTitle());
    }

    public List<ReplySummary> replySummaries() {
        return replies.stream()
                .map(r -> new ReplySummary(r.getId(), r.getOwner().getOwnerName(), r.getContent(), r.getCreatedAt()))
                .sorted(Comparator.comparing(ReplySummary::getCreatedAt))
                .collect(Collectors.toList());
    }

}
