package me.scene.dinner.like.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUsernameAndArticleId(String username, Long articleId);

    Optional<Like> findByUsernameAndArticleId(String username, Long articleId);

}
