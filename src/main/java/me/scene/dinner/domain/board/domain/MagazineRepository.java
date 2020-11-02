package me.scene.dinner.domain.board.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    Optional<Magazine> findByTitle(String title);

}
