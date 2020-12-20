package me.scene.dinner.tag.domain;

import me.scene.dinner.board.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface TaggedArticleRepository extends JpaRepository<TaggedArticle, Long> {

    Set<TaggedArticle> findByArticle(Article article);

}
