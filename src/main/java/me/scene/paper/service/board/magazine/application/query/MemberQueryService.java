package me.scene.paper.service.board.magazine.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.service.board.magazine.domain.magazine.repository.MagazineRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MagazineRepository repository;

    public List<String> memberNames(Long id) {
        Magazine magazine = repository.find(id);
        return magazine.memberNames();
    }

}
