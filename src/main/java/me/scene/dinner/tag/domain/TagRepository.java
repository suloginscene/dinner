package me.scene.dinner.tag.domain;

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
