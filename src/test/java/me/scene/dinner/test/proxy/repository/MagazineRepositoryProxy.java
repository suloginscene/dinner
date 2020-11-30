package me.scene.dinner.test.proxy.repository;

import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;

import java.util.Optional;

public interface MagazineRepositoryProxy extends MagazineRepository {

    Optional<Magazine> findByTitle(String title);

}
