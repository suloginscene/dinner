package me.scene.dinner.board.article.domain.tag.repository;

import me.scene.dinner.board.article.domain.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByName(String name);

    Optional<Tag> findByName(String name);


    default Tag find(String name) {
        return findByName(name).orElseThrow(() -> new NoSuchElementException(name));
    }

}
