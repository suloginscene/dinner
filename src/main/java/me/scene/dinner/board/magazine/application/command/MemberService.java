package me.scene.dinner.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
import me.scene.dinner.board.magazine.domain.managed.ManagedMagazine;
import me.scene.dinner.board.magazine.domain.managed.ManagedEvent;
import me.scene.dinner.board.magazine.domain.managed.MemberQuitEvent;
import me.scene.dinner.board.magazine.domain.managed.MemberAppliedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static me.scene.dinner.board.magazine.domain.common.Type.MANAGED;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MagazineRepository repository;

    private final ApplicationEventPublisher publisher;


    public List<String> memberNames(Long id) {
        ManagedMagazine magazine = findManagedMagazine(id);
        return magazine.memberNames();
    }


    public void applyMember(Long id, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        Optional<MemberAppliedEvent> event = magazine.apply(memberName);
        event.ifPresent(publisher::publishEvent);
    }

    public void quitMember(Long id, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        Optional<MemberQuitEvent> event = magazine.quit(memberName);
        event.ifPresent(publisher::publishEvent);
    }


    public void addMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        Optional<ManagedEvent> event = magazine.addMember(ownerName, memberName);
        event.ifPresent(publisher::publishEvent);
    }

    public void removeMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        Optional<ManagedEvent> event = magazine.removeMember(ownerName, memberName);
        event.ifPresent(publisher::publishEvent);
    }


    private ManagedMagazine findManagedMagazine(Long id) {
        Magazine magazine = repository.find(id);
        magazine.type().check(MANAGED);
        return (ManagedMagazine) magazine;
    }

}
