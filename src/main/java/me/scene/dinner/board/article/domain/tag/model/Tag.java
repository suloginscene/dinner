package me.scene.dinner.board.article.domain.tag.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.article.domain.article.model.ArticleTag;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Tag extends BaseEntity {

    @Column(length = 16, unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "tag")
    private final Set<ArticleTag> articleTags = new HashSet<>();


    public Tag(String name) {
        this.name = name;
    }

    public Set<ArticleTag> publicTaggedArticles() {
        return articleTags.stream()
                .filter(taggedArticle -> taggedArticle.getArticle().isPublicized())
                .collect(toSet());
    }

}
