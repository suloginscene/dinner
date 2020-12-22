package me.scene.dinner.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.command.event.MemberAddedEvent;
import me.scene.dinner.board.magazine.application.command.event.MemberAppliedEvent;
import me.scene.dinner.board.magazine.application.command.event.MemberQuitEvent;
import me.scene.dinner.board.magazine.application.command.event.MemberRemovedEvent;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;
import me.scene.dinner.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.dinner.board.magazine.domain.managed.model.ManagedMagazine;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static me.scene.dinner.board.magazine.domain.magazine.model.Type.MANAGED;


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
        if (magazine.apply(memberName)) {
            MemberAppliedEvent event = new MemberAppliedEvent(magazine, memberName);
            publisher.publishEvent(event);
        }
    }

    public void quitMember(Long id, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        if (magazine.quit(memberName)) {
            MemberQuitEvent event = new MemberQuitEvent(magazine, memberName);
            publisher.publishEvent(event);
        }
    }


    public void addMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        if (magazine.addMember(ownerName, memberName)) {
            MemberAddedEvent event = new MemberAddedEvent(magazine, memberName);
            publisher.publishEvent(event);
        }
    }

    public void removeMember(Long id, String ownerName, String memberName) {
        ManagedMagazine magazine = findManagedMagazine(id);
        if (magazine.removeMember(ownerName, memberName)) {
            MemberRemovedEvent event = new MemberRemovedEvent(magazine, memberName);
            publisher.publishEvent(event);
        }
    }


    private ManagedMagazine findManagedMagazine(Long id) {
        Magazine magazine = repository.find(id);
        magazine.type().check(MANAGED);
        return (ManagedMagazine) magazine;
    }

}
