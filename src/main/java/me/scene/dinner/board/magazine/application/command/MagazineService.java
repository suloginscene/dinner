package me.scene.dinner.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.common.BoardNotFoundException;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.application.command.request.MagazineCreateRequest;
import me.scene.dinner.board.magazine.application.command.request.MagazineUpdateRequest;
import me.scene.dinner.board.magazine.domain.ExclusiveMagazine;
import me.scene.dinner.board.magazine.domain.ManagedMagazine;
import me.scene.dinner.board.magazine.domain.OpenMagazine;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
import me.scene.dinner.board.magazine.domain.common.Type;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MagazineService {

    private final MagazineRepository repository;

    private final ApplicationEventPublisher publisher;


    public Long save(MagazineCreateRequest request) {
        Magazine magazine = createMagazine(request);
        Long id = repository.save(magazine).getId();
        publishMagazineUpdatedEvent(id);
        return id;
    }

    public void update(Long id, MagazineUpdateRequest request) {
        Magazine magazine = find(id);
        updateMagazine(magazine, request);
        publishMagazineUpdatedEvent(id);
    }

    public void delete(Long id, String current) {
        Magazine magazine = find(id);
        magazine.beforeDelete(current);
        repository.delete(magazine);
        publishMagazineUpdatedEvent(id);
    }


    // private ---------------------------------------------------------------------------------------------------------

    private Magazine find(Long id) {
        return repository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }


    private Magazine createMagazine(MagazineCreateRequest r) {
        Owner owner = new Owner(r.getOwnerName());
        String title = r.getTitle();
        String shortExplanation = r.getShortExplanation();
        String longExplanation = r.getLongExplanation();

        switch (Type.valueOf(r.getType())) {
            case OPEN:
                return new OpenMagazine(owner, title, shortExplanation, longExplanation);
            case MANAGED:
                return new ManagedMagazine(owner, title, shortExplanation, longExplanation);
            case EXCLUSIVE:
                return new ExclusiveMagazine(owner, title, shortExplanation, longExplanation);
            default:
                throw new IllegalStateException();
        }
    }

    private void updateMagazine(Magazine magazine, MagazineUpdateRequest r) {
        magazine.update(r.getCurrentUsername(), r.getTitle(), r.getShortExplanation(), r.getLongExplanation());
    }


    // to Update cache
    private void publishMagazineUpdatedEvent(Long id) {
        MagazineUpdatedEvent event = new MagazineUpdatedEvent(id);
        publisher.publishEvent(event);
    }

}
