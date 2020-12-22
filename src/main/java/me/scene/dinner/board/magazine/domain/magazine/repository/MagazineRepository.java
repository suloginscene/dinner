package me.scene.dinner.board.magazine.domain.magazine.repository;

import me.scene.dinner.board.common.domain.model.Owner;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    List<Magazine> findByOwnerOrderByPointDesc(Owner owner);


    default Magazine find(Long id) {
        return findById(id).orElseThrow();
    }

}
