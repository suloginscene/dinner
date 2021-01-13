package me.scene.paper.service.board.magazine.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.magazine.application.command.request.MagazineCreateRequest;
import me.scene.paper.service.board.magazine.application.command.request.MagazineUpdateRequest;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.service.board.magazine.domain.magazine.model.Type;
import me.scene.paper.service.board.magazine.domain.magazine.repository.MagazineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MagazineService {

    private final MagazineRepository repository;
    private final MagazineCache cache;


    public Long save(MagazineCreateRequest request) {
        String username = request.getUsername();
        String title = request.getTitle();
        String shortExplanation = request.getShortExplanation();
        String longExplanation = request.getLongExplanation();
        String typeString = request.getType();

        Type type = Type.valueOf(typeString);

        Magazine magazine = Magazine.create(type, username, title, shortExplanation, longExplanation);
        Long id = repository.save(magazine).getId();

        cache.update(id);

        return id;
    }

    public void update(Long id, MagazineUpdateRequest request) {
        String username = request.getUsername();
        String title = request.getTitle();
        String shortExplanation = request.getShortExplanation();
        String longExplanation = request.getLongExplanation();

        Magazine magazine = repository.find(id);
        magazine.update(username, title, shortExplanation, longExplanation);

        cache.update(id);
    }

    public void delete(Long id, String current) {
        Magazine magazine = repository.find(id);
        magazine.beforeDelete(current);
        repository.delete(magazine);

        cache.update(id);
    }

}
