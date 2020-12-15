package me.scene.dinner.board.application.magazine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.domain.magazine.Magazine;
import me.scene.dinner.board.domain.magazine.MagazineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor @Slf4j
public class MagazineService {

    private final MagazineBestListCache bestListCache;
    private final MagazineRepository magazineRepository;

    private void updateCache() {
        List<MagazineDto> allMagazines = all();
        List<MagazineDto> magazineDtos = (allMagazines.size() > bestListCache.getSize()) ?
                allMagazines.subList(0, bestListCache.getSize()) : allMagazines;
        bestListCache.setBestMagazines(magazineDtos);
    }


    public Magazine find(Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new MagazineNotFoundException(id));
    }

    public List<MagazineDto> all() {
        return magazineRepository.findAll().stream()
                .sorted((m, o) -> o.getRating() - m.getRating())
                .map(this::extractDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public Long save(String manager, String title, String shortExplanation, String longExplanation, String policy) {
        Magazine magazine = Magazine.create(manager, title, shortExplanation, longExplanation, policy);
        Long id = magazineRepository.save(magazine).getId();
        updateCache();
        return id;
    }

    public MagazineDto read(Long id) {
        Magazine magazine = find(id);
        return extractDto(magazine);
    }

    public MagazineDto findToUpdate(Long id, String current) {
        Magazine magazine = find(id);
        magazine.confirmManager(current);
        return extractDto(magazine);
    }

    public MagazineDto findToManageMember(Long id, String current) {
        Magazine magazine = find(id);
        magazine.confirmManager(current);
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
        magazine.applyMember(current);
        publishEvent(magazine);
    }

    @Transactional
    public void quitMember(Long id, String current) {
        Magazine magazine = find(id);
        magazine.quitMember(current);
        publishEvent(magazine);
    }

    @Transactional
    public void addMember(Long id, String current, String target) {
        Magazine magazine = find(id);
        magazine.addMember(current, target);
        publishEvent(magazine);
    }

    @Transactional
    public void removeMember(Long id, String current, String target) {
        Magazine magazine = find(id);
        magazine.removeMember(current, target);
        publishEvent(magazine);
    }

    private void publishEvent(Magazine magazine) {
        magazineRepository.save(magazine);
    }

    public List<MagazineDto> findByManager(String username) {
        return magazineRepository.findByManagerOrderByRatingDesc(username).stream()
                .map(this::extractDto).collect(Collectors.toList());
    }

    protected MagazineDto extractDto(Magazine m) {

        // TODO fetch join
        List<String> members = m.getMembers();
        log.info("load: {}", members);
        List<String> writers = m.getWriters();
        log.info("load: {}", writers);

        return new MagazineDto(m.getId(), m.getManager(), m.getTitle(), m.getShortExplanation(), m.getLongExplanation(),
                m.getPolicy().name(), members, writers, m.getTopicSummaries());
    }

}
