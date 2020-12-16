package me.scene.dinner.board.article.domain;

import me.scene.dinner.board.common.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByOwnerAndPublicizedOrderByRatingDesc(Owner owner, boolean publicized);

    List<Article> findByOwnerAndPublicizedOrderByCreatedAtAsc(Owner owner, boolean publicized);

}
