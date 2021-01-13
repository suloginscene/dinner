package me.scene.paper.service.board.article.domain.article.repository;

import me.scene.paper.service.board.article.domain.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a" +
            " join fetch a.topic t" +
            " join fetch t.magazine" +
            " where a.id = :id")
    Article fetchToRate(@Param("id") Long id);

    @Query("select a from Article a" +
            " join fetch a.topic t" +
            " where a.id = :id")
    Article fetchToView(@Param("id") Long id);

    @Query("select distinct a from Article a" +
            " where a.owner.name = :owner and a.publicized = true" +
            " order by a.point desc")
    List<Article> findByUsername(@Param("owner") String owner);

    @Query("select distinct a from Article a" +
            " where a.owner.name = :owner and a.publicized = false" +
            " order by a.createdAt")
    List<Article> findPrivateByUsername(@Param("owner") String owner);

    @Query("select distinct a" +
            " from Article a" +
            " where a.topic.id = :topicId and a.publicized = true" +
            " order by a.createdAt")
    List<Article> findPublicByTopicId(@Param("topicId") Long topicId);


    default Article find(Long id) {
        return findById(id).orElseThrow();
    }

}
