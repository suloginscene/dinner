package me.scene.dinner.board.article.domain.article.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.article.domain.tag.model.Tag;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = PROTECTED)
public class ArticleTag extends BaseEntity {

    @ManyToOne(fetch = EAGER)
    private Article article;

    @ManyToOne(fetch = EAGER)
    private Tag tag;


    public ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }

}
