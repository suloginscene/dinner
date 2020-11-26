package me.scene.dinner.test.proxy;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
public class MagazineServiceProxy extends MagazineService {

    public MagazineServiceProxy(MagazineRepository magazineRepository) {
        super(magazineRepository);
    }

    public List<String> getMembers(Long id) {
        Magazine magazine = find(id);
        List<String> members = magazine.getMembers();
        log.debug("load " + members);
        return members;
    }

}
