package me.scene.dinner.board.magazine.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.board.magazine.domain.Writer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor @Slf4j
public class MagazineService {

    private final MagazineBestListCache bestListCache;
    private final MagazineRepository magazineRepository;
    private final ApplicationEventPublisher eventPublisher;

    private void updateCache() {
        List<MagazineSimpleDto> allMagazines = all();
        List<MagazineSimpleDto> magazineDtos = (allMagazines.size() > bestListCache.getSize()) ?
                allMagazines.subList(0, bestListCache.getSize()) : allMagazines;
        bestListCache.setBestMagazines(magazineDtos);
    }


    public Magazine find(Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new MagazineNotFoundException(id));
    }

    public List<MagazineSimpleDto> all() {
        return magazineRepository.findAll().stream()
                .sorted((m, o) -> o.getRating() - m.getRating())
                .map(this::extractDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public Long save(String manager, String title, String shortExplanation, String longExplanation, String policy) {
        Magazine magazine = new Magazine(manager, title, shortExplanation, longExplanation, policy);
        Long id = magazineRepository.save(magazine).getId();
        updateCache();
        return id;
    }

    public MagazineSimpleDto read(Long id) {
        Magazine magazine = find(id);
        return extractDto(magazine);
    }

    public MagazineSimpleDto findToUpdate(Long id, String current) {
        Magazine magazine = find(id);
        magazine.getOwner().identify(current);
        return extractDto(magazine);
    }

    public MagazineSimpleDto findToManageMember(Long id, String current) {
        Magazine magazine = find(id);
        magazine.getOwner().identify(current);
        magazine.confirmPolicyManaged();
        return extractDto(magazine);
    }

    @Transactional
    public void update(Long id, String current, String title, String shortExplanation, String longExplanation) {
        Magazine magazine = find(id);
        magazine.update(current, title, shortExplanation, longExplanation);
        updateCache();
    }

    @Transactional
    public void delete(Long id, String current) {
        Magazine magazine = find(id);
        magazine.beforeDelete(current);
        magazineRepository.delete(magazine);
        updateCache();
    }

    @Transactional
    public void applyMember(Long id, String current) {
        Magazine magazine = find(id);
        magazine.applyMember(current).ifPresent(eventPublisher::publishEvent);
    }

    @Transactional
    public void quitMember(Long id, String current) {
        Magazine magazine = find(id);
        magazine.quitMember(current).ifPresent(eventPublisher::publishEvent);
    }

    @Transactional
    public void addMember(Long id, String current, String target) {
        Magazine magazine = find(id);
        magazine.addMember(current, target).ifPresent(eventPublisher::publishEvent);
    }

    @Transactional
    public void removeMember(Long id, String current, String target) {
        Magazine magazine = find(id);
        magazine.removeMember(current, target).ifPresent(eventPublisher::publishEvent);
    }

    public List<MagazineSimpleDto> findByManager(String username) {
        return magazineRepository.findByOwnerOrderByRatingDesc(new Owner(username)).stream()
                .map(this::extractDto).collect(Collectors.toList());
    }

    protected MagazineSimpleDto extractDto(Magazine m) {

        // TODO fetch join
        List<String> members = m.getMembers();
        log.info("load: {}", members);
        List<String> writers = m.getWriters().stream().map(Writer::getWriterName).collect(Collectors.toList());
        log.info("load: {}", writers);

        return new MagazineSimpleDto(m.getId(), m.getOwner().getOwnerName(), m.getTitle(), m.getShortExplanation(), m.getLongExplanation(),
                m.getPolicy().name(), members, writers, m.getTopicCount());
    }

}
