package me.scene.dinner.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByUsernameAndArticleId(String username, Long articleId);

    Likes findByUsernameAndArticleId(String username, Long articleId);

}
