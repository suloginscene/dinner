package me.scene.dinner.board.magazine.domain.common;

import me.scene.dinner.board.common.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    List<Magazine> findByOwnerOrderByPointDesc(Owner owner);

}
