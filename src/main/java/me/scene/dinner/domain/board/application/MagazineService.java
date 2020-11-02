package me.scene.dinner.domain.board.application;

import me.scene.dinner.domain.board.domain.Magazine;
import me.scene.dinner.domain.board.domain.MagazineRepository;
import me.scene.dinner.infra.exception.BoardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
public class MagazineService {

    private final MagazineRepository magazineRepository;

    @Autowired
    public MagazineService(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    @Transactional
    public Long save(Magazine magazine) {
        Magazine saved = magazineRepository.save(magazine);
        return saved.getId();
    }

    public Magazine find(Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new BoardNotFoundException("magazine", "id", id.toString()));
    }

    public Magazine find(String title) {
        return magazineRepository.findByTitle(title).orElseThrow(() -> new BoardNotFoundException("magazine", "title", title));
    }

}
