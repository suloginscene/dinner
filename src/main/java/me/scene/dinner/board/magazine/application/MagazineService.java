package me.scene.dinner.board.magazine.application;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
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
        return magazineRepository.findById(id).orElseThrow(() -> new MagazineNotFoundException(id));
    }

    public List<MagazineDto> findAllAsDto() {
        return magazineRepository.findAll().stream()
                .map(this::extractDto)
                .collect(Collectors.toList());
    }

    public MagazineDto extractDto(Long magazineId) {
        Magazine magazine = magazineRepository.findById(magazineId).orElseThrow(() -> new MagazineNotFoundException(magazineId));
        return extractDto(magazine);
    }

    public MagazineDto extractDto(Magazine magazine) {
        String manager = accountService.find(magazine.getManagerId()).getUsername();
        List<String> writers = magazine.getWriterIds().stream()
                .map((id) -> accountService.find(id).getUsername())
                .collect(Collectors.toList());
        List<String> topicManagers = magazine.getTopics().stream()
                .map(t -> accountService.find(t.getManagerId()).getUsername())
                .collect(Collectors.toList());

        return MagazineDto.create(magazine.getId(), manager, writers,
                magazine.getTitle(), magazine.getShortExplanation(), magazine.getLongExplanation(),
                magazine.getMagazinePolicy().name(), magazine.getTopics(), topicManagers);
    }

}
