package me.scene.dinner.test.proxy.repository;

import me.scene.dinner.board.domain.magazine.Magazine;
import me.scene.dinner.board.domain.magazine.MagazineRepository;

import java.util.Optional;

public interface MagazineRepositoryProxy extends MagazineRepository {

    Optional<Magazine> findByTitle(String title);

}
