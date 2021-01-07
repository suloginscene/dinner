package me.scene.paper.board.article.domain.article.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.board.common.domain.model.Board;
import me.scene.paper.board.common.domain.model.Owner;
import me.scene.paper.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.board.magazine.domain.open.model.OpenMagazine;
import me.scene.paper.board.topic.domain.model.Topic;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.paper.board.magazine.domain.magazine.model.Type.OPEN;


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
        topic.getMagazine().authorization().check(owner);

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
        rate(-point.get());
        topic.getArticles().remove();

        logWriter();
    }


    private void logWriter() {
        Magazine magazine = topic.getMagazine();

        if (magazine.type() != OPEN) return;

        String writer = owner.name();
        if (magazine.getOwner().is(writer)) return;

        OpenMagazine open = (OpenMagazine) magazine;

        if (publicized) open.logWriting(writer);
        else open.logErasing(writer);
    }


    public void read() {
        read++;
    }

    @Override
    protected void propagateRate(int point) {
        topic.rate(point);
    }

}
