package me.scene.dinner.board.article.query;

import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.common.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleQueryRepository extends JpaRepository<Article, Long> {

    List<Article> findByOwnerAndPublicizedOrderByPointDesc(Owner owner, boolean publicized);

    List<Article> findByOwnerAndPublicizedOrderByCreatedAtAsc(Owner owner, boolean publicized);

}
