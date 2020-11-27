package me.scene.dinner.test.proxy;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.board.magazine.domain.Member;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
public class MagazineServiceProxy extends MagazineService {

    private final MagazineRepository magazineRepository;

    public MagazineServiceProxy(MagazineRepository magazineRepository) {
        super(magazineRepository);
        this.magazineRepository = magazineRepository;
    }

    public Magazine load(String title) {
        Magazine magazine = magazineRepository.findByTitle(title).orElseThrow();
        log.debug("load: " + magazine.getWriters());
        return magazine;
    }

    public List<String> getMembers(Long id) {
        Magazine magazine = find(id);
        return magazine.getMembers().stream().map(Member::getUsername).collect(Collectors.toList());
    }

    public void addMember(Magazine magazine, Account member) {
        addMember(magazine.getId(), magazine.getManager(), member.getUsername(), member.getEmail());
    }

}
