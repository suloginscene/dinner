package me.scene.dinner.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.domain.ManagedMagazine;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static me.scene.dinner.board.magazine.domain.common.Magazine.Type.MANAGED;


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
        Optional<ManagedMagazine.Member.ManagerEvent> event = magazine.apply(memberName);
        event.ifPresent(publisher::publishEvent);
    }

    public void quitMember(Long id, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        Optional<ManagedMagazine.Member.ManagerEvent> event = magazine.quit(memberName);
        event.ifPresent(publisher::publishEvent);
    }


    public void addMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        Optional<ManagedMagazine.Member.MemberEvent> event = magazine.addMember(ownerName, memberName);
        event.ifPresent(publisher::publishEvent);
    }

    public void removeMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        Optional<ManagedMagazine.Member.MemberEvent> event = magazine.removeMember(ownerName, memberName);
        event.ifPresent(publisher::publishEvent);
    }


    private ManagedMagazine findManagedMagazine(Long id) {
        Magazine magazine = repository.find(id);
        magazine.checkType(MANAGED);
        return (ManagedMagazine) magazine;
    }

}
