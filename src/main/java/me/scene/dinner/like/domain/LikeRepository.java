package me.scene.dinner.like.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUsernameAndArticleId(String username, Long articleId);

    @Query("select l from Like l" +
            " join fetch l.article a" +
            " join fetch a.topic" +
            " join fetch a.magazine" +
            " where l.username = :username and a.id = :articleId")
    Optional<Like> findToDislike(@Param("username") String username, @Param("articleId") Long articleId);

}
