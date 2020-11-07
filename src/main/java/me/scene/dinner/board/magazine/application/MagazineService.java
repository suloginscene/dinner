package me.scene.dinner.board.magazine.application;

import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional(readOnly = true)
public class MagazineService {

    private final MagazineRepository magazineRepository;

    @Autowired
    public MagazineService(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    @Transactional
    public Long save(String manager, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Magazine magazine = Magazine.create(manager, title, shortExplanation, longExplanation, magazinePolicy);
        magazine = magazineRepository.save(magazine);
        return magazine.getId();
    }

    public Magazine find(Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new MagazineNotFoundException(id));
    }

    public List<Magazine> findAll() {
        return magazineRepository.findAll();
    }

}
