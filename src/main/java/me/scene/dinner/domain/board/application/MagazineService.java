package me.scene.dinner.domain.board.application;

import me.scene.dinner.domain.account.application.AccountService;
import me.scene.dinner.domain.board.domain.Magazine;
import me.scene.dinner.domain.board.domain.MagazineRepository;
import me.scene.dinner.infra.exception.BoardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional(readOnly = true)
public class MagazineService {

    private final MagazineRepository magazineRepository;
    private final AccountService accountService;

    @Autowired
    public MagazineService(MagazineRepository magazineRepository, AccountService accountService) {
        this.magazineRepository = magazineRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Long save(Long managerId, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Magazine magazine = Magazine.create(managerId, title, shortExplanation, longExplanation, magazinePolicy);
        magazine = magazineRepository.save(magazine);
        return magazine.getId();
    }

    public Magazine find(Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new BoardNotFoundException("magazine", "id", id.toString()));
    }

    public MagazineDto extractDto(Long magazineId) {
        Magazine magazine = magazineRepository.findById(magazineId).orElseThrow(() -> new BoardNotFoundException("magazine", "id", magazineId.toString()));

        String manager = accountService.find(magazine.getManagerId()).getUsername();
        List<String> writers = magazine.getWriterIds().stream()
                .map((id) -> accountService.find(id).getUsername())
                .collect(Collectors.toList());
        return MagazineDto.create(magazineId, manager, writers,
                magazine.getTitle(), magazine.getShortExplanation(), magazine.getLongExplanation(), magazine.getMagazinePolicy().name());
    }

}
