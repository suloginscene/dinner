package me.scene.dinner.board.domain.article;

import me.scene.dinner.board.domain.common.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByOwnerAndPublicizedOrderByRatingDesc(Owner owner, boolean publicized);

    List<Article> findByOwnerAndPublicizedOrderByCreatedAtAsc(Owner owner, boolean publicized);

}
