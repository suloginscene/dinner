package me.scene.paper.service.integration.utils;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.magazine.application.command.MagazineService;
import me.scene.paper.service.board.magazine.application.command.request.MagazineCreateRequest;
import me.scene.paper.service.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.paper.service.board.magazine.domain.magazine.model.Type;
import me.scene.paper.service.integration.utils.aop.LogAround;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MagazineTestHelper {

    private final MagazineService service;

    private final MagazineRepository repository;


    @LogAround
    public Long createMagazine(String ownerName, String title, Type type) {
        MagazineCreateRequest request = new MagazineCreateRequest(ownerName, title, "shortExplanation", "longExplanation", type.name());
        return service.save(request);
    }

    @LogAround
    public void clearMagazines() {
        repository.deleteAll();
    }

}
