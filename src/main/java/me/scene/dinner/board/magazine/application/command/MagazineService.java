package me.scene.dinner.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.command.request.MagazineCreateRequest;
import me.scene.dinner.board.magazine.application.command.request.MagazineUpdateRequest;
import me.scene.dinner.board.magazine.application.command.event.MagazineDatabaseChangedEvent;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;
import me.scene.dinner.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.dinner.board.magazine.domain.magazine.model.Type;
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
        String username = request.getUsername();
        String title = request.getTitle();
        String shortExplanation = request.getShortExplanation();
        String longExplanation = request.getLongExplanation();
        String typeString = request.getType();

        Type type = Type.valueOf(typeString);

        Magazine magazine = Magazine.create(type, username, title, shortExplanation, longExplanation);
        Long id = repository.save(magazine).getId();

        MagazineDatabaseChangedEvent event = new MagazineDatabaseChangedEvent(id);
        publisher.publishEvent(event);

        return id;
    }

    public void update(Long id, MagazineUpdateRequest request) {
        String username = request.getUsername();
        String title = request.getTitle();
        String shortExplanation = request.getShortExplanation();
        String longExplanation = request.getLongExplanation();

        Magazine magazine = repository.find(id);
        magazine.update(username, title, shortExplanation, longExplanation);

        MagazineDatabaseChangedEvent event = new MagazineDatabaseChangedEvent(id);
        publisher.publishEvent(event);
    }

    public void delete(Long id, String current) {
        Magazine magazine = repository.find(id);
        magazine.beforeDelete(current);
        repository.delete(magazine);

        MagazineDatabaseChangedEvent event = new MagazineDatabaseChangedEvent(id);
        publisher.publishEvent(event);
    }

}
