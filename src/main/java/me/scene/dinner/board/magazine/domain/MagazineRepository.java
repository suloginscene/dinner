package me.scene.dinner.board.magazine.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    List<Magazine> findByManager(String manager);

}
