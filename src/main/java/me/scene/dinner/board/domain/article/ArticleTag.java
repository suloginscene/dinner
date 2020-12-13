package me.scene.dinner.board.domain.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @EqualsAndHashCode(of = "name")
public class ArticleTag {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    protected ArticleTag() {
    }

    public static ArticleTag create(String name) {
        ArticleTag articleTag = new ArticleTag();
        articleTag.name = name;
        return articleTag;
    }

}
