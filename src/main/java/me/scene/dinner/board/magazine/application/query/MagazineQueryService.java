package me.scene.dinner.board.magazine.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.common.domain.model.Owner;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import me.scene.dinner.board.magazine.application.query.dto.MagazineView;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;
import me.scene.dinner.board.magazine.domain.magazine.repository.MagazineRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MagazineQueryService {

    private final MagazineRepository repository;


    public MagazineView view(Long id) {
        Magazine magazine = repository.find(id);
        return new MagazineView(magazine);
    }


    public List<MagazineLink> linksOfUser(String username) {
        Owner owner = new Owner(username);
        List<Magazine> magazines = repository.findByOwnerOrderByPointDesc(owner);
        return magazines.stream()
                .map(MagazineLink::new)
                .collect(toList());
    }

    public List<MagazineLink> allLinks() {
        List<Magazine> magazines = repository.findAll();
        return magazines.stream()
                .map(MagazineLink::new)
                .collect(toList());
    }

    public List<MagazineLink> bestLinks(int size) {
        Slice<Magazine> magazines = repository.findAll(PageRequest.of(0, size, by(DESC, "point")));
        return magazines
                .map(MagazineLink::new)
                .getContent();
    }

}
