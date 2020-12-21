package me.scene.dinner.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
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
        String username = request.getUsername();
        String title = request.getTitle();
        String shortExplanation = request.getShortExplanation();
        String longExplanation = request.getLongExplanation();
        String typeString = request.getType();

        Type type = Type.valueOf(typeString);

        Magazine magazine = Magazine.create(type, username, title, shortExplanation, longExplanation);
        Long id = repository.save(magazine).getId();

        ChangedEvent event = magazine.changedEvent();
        publisher.publishEvent(event);

        return id;
    }

    public void update(Long id, MagazineUpdateRequest request) {
        String username = request.getUsername();
        String title = request.getTitle();
        String shortExplanation = request.getShortExplanation();
        String longExplanation = request.getLongExplanation();

        Magazine magazine = repository.find(id);
        ChangedEvent event = magazine.update(username, title, shortExplanation, longExplanation);

        publisher.publishEvent(event);
    }

    public void delete(Long id, String current) {
        Magazine magazine = repository.find(id);
        ChangedEvent event = magazine.beforeDelete(current);
        repository.delete(magazine);

        publisher.publishEvent(event);
    }

}
