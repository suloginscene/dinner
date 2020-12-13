package me.scene.dinner.tag;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.domain.article.Article;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class TaggedArticle {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Article article;

    protected TaggedArticle() {
    }

    public static TaggedArticle create(Article article) {
        TaggedArticle taggedArticle = new TaggedArticle();
        taggedArticle.article = article;
        return taggedArticle;
    }

}
