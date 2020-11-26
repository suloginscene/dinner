package me.scene.dinner.board.magazine.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.magazine.domain.MagazineRepository;
import me.scene.dinner.board.magazine.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class MagazineService {

    private final MagazineRepository magazineRepository;

    public Magazine find(Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new MagazineNotFoundException(id));
    }

    public List<Magazine> findAll() {
        return magazineRepository.findAll();
    }

    public List<Magazine> findBest() {
        // TODO findBest
        return magazineRepository.findAll();
    }

    @Transactional
    public Long save(String manager, String managerEmail, String title, String shortExplanation, String longExplanation, String policy) {
        Magazine magazine = Magazine.create(manager, managerEmail, title, shortExplanation, longExplanation, policy);
        return magazineRepository.save(magazine).getId();
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
    public void addMember(Long id, String current, String target, String targetEmail) {
        Magazine magazine = find(id);
        magazine.addMember(current, new Member(target, targetEmail));
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

}
