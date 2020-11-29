package me.scene.dinner.board.magazine.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    Optional<Magazine> findByTitle(String title);

    List<Magazine> findByManager(String manager);

}
