package me.scene.dinner.test.proxy.service;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.test.proxy.repository.MagazineRepositoryProxy;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
public class MagazineServiceProxy extends MagazineService {

    private final MagazineRepositoryProxy magazineRepository;

    public MagazineServiceProxy(MagazineRepositoryProxy magazineRepository) {
        super(magazineRepository);
        this.magazineRepository = magazineRepository;
    }

    public Magazine load(String title) {
        Magazine magazine = magazineRepository.findByTitle(title).orElseThrow();
        log.debug("load: " + magazine.getWriters());
        log.debug("load: " + magazine.getMembers());
        return magazine;
    }

    public void addMember(Magazine magazine, Account member) {
        addMember(magazine.getId(), magazine.getManager(), member.getUsername());
    }

}
