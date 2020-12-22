package me.scene.dinner.board.article.domain.article.repository;

import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.common.domain.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByOwnerAndPublicizedOrderByPointDesc(Owner owner, boolean publicized);

    List<Article> findByOwnerAndPublicizedOrderByCreatedAtAsc(Owner owner, boolean publicized);

    @Query("select a from Article a" +
            " join fetch a.topic t" +
            " join fetch t.magazine" +
            " where a.id = :id")
    Article findFetch(@Param("id") Long id);


    default Article find(Long id) {
        return findById(id).orElseThrow();
    }

}
