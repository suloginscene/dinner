package me.scene.dinner.tag.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TaggedArticle extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Article article;

    public TaggedArticle(Article article) {
        this.article = article;
    }

    public boolean isPublicized() {
        return article.isPublicized();
    }

}
