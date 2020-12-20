package me.scene.dinner.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.common.domain.Owner;
import me.scene.dinner.board.magazine.application.command.request.MagazineCreateRequest;
import me.scene.dinner.board.magazine.application.command.request.MagazineUpdateRequest;
import me.scene.dinner.board.magazine.domain.common.ChangedEvent;
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

        ChangedEvent event = magazine.changedEvent();
        publisher.publishEvent(event);
        return id;
    }

    public void update(Long id, MagazineUpdateRequest request) {
        Magazine magazine = repository.find(id);
        ChangedEvent event = updateMagazine(magazine, request);
        publisher.publishEvent(event);
    }

    public void delete(Long id, String current) {
        Magazine magazine = repository.find(id);
        ChangedEvent event = magazine.beforeDelete(current);
        repository.delete(magazine);
        publisher.publishEvent(event);
    }


    // private ---------------------------------------------------------------------------------------------------------

    private Magazine createMagazine(MagazineCreateRequest r) {
        Owner owner = new Owner(r.getOwnerName());
        String title = r.getTitle();
        String shortExplanation = r.getShortExplanation();
        String longExplanation = r.getLongExplanation();

        Type type = Type.valueOf(r.getType());
        return Magazine.create(type, owner, title, shortExplanation, longExplanation);
    }

    private ChangedEvent updateMagazine(Magazine magazine, MagazineUpdateRequest r) {
        return magazine.update(r.getCurrentUsername(), r.getTitle(), r.getShortExplanation(), r.getLongExplanation());
    }

}
