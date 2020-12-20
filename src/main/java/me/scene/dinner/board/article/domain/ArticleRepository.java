package me.scene.dinner.board.article.domain;

import me.scene.dinner.board.common.domain.BoardNotFoundException;
import me.scene.dinner.board.common.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByOwnerAndPublicizedOrderByPointDesc(Owner owner, boolean publicized);

    List<Article> findByOwnerAndPublicizedOrderByCreatedAtAsc(Owner owner, boolean publicized);


    default Article find(Long id) {
        return findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }

}
