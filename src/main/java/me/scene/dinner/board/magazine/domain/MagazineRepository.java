package me.scene.dinner.board.magazine.domain;

import me.scene.dinner.board.common.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    List<Magazine> findByOwnerOrderByRatingDesc(Owner owner);

}
