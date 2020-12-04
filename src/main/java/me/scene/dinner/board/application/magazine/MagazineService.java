package me.scene.dinner.board.application.magazine;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.domain.magazine.Magazine;
import me.scene.dinner.board.domain.magazine.MagazineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class MagazineService {

    private final MagazineRepository magazineRepository;

    public Magazine find(Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new MagazineNotFoundException(id));
    }

    public List<Magazine> findAll() {
        List<Magazine> allMagazines = magazineRepository.findAll();
        allMagazines.sort(Comparator.comparing(Magazine::getRating, Comparator.reverseOrder()));
        return allMagazines;
    }

    public List<Magazine> findBest(int count) {
        List<Magazine> allMagazines = findAll();
        return (allMagazines.size() > count) ?
                allMagazines.subList(0, count) : allMagazines;
    }

    @Transactional
    public Long save(String manager, String title, String shortExplanation, String longExplanation, String policy) {
        Magazine magazine = Magazine.create(manager, title, shortExplanation, longExplanation, policy);
        return magazineRepository.save(magazine).getId();
    }

    public Magazine read(Long id) {
        Magazine magazine = find(id);
        // TODO
        magazine.getTopics().sort(Comparator.comparing(t -> t.getRating(), Comparator.reverseOrder()));
        return magazine;
    }

    public Magazine findToUpdate(Long id, String current) {
        Magazine magazine = find(id);
        magazine.confirmManager(current);
        return magazine;
    }

    public Magazine findToManageMember(Long id, String current) {
        Magazine magazine = find(id);
        magazine.confirmManager(current);
        magazine.confirmPolicyManaged();
        return magazine;
    }

    @Transactional
    public void update(Long id, String current, String title, String shortExplanation, String longExplanation) {
        Magazine magazine = find(id);
        magazine.update(current, title, shortExplanation, longExplanation);
        publishEvent(magazine);
    }

    @Transactional
    public void delete(Long id, String current) {
        Magazine magazine = find(id);
        magazine.beforeDelete(current);
        publishEvent(magazine);
        magazineRepository.delete(magazine);
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

    public List<Magazine> findByManager(String username) {
        return magazineRepository.findByManagerOrderByRatingDesc(username);
    }

}
