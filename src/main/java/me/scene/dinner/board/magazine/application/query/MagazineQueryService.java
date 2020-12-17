package me.scene.dinner.board.magazine.application.query;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.common.BoardNotFoundException;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import me.scene.dinner.board.magazine.application.query.dto.MagazineSimpleDto;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
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

    private final MagazineRepository magazineRepository;


    public MagazineSimpleDto findById(Long id) {
        Magazine magazine = find(id);
        return new MagazineSimpleDto(magazine);
    }


    public List<MagazineSimpleDto> findByUsername(String username) {
        Owner owner = new Owner(username);
        List<Magazine> magazines = magazineRepository.findByOwnerOrderByPointDesc(owner);
        return magazines.stream()
                .map(MagazineSimpleDto::new)
                .collect(toList());
    }

    public List<MagazineSimpleDto> findAll() {
        List<Magazine> all = magazineRepository.findAll();
        return all.stream().map(MagazineSimpleDto::new).collect(toList());
    }

    public List<MagazineLink> findBest(int size) {
        PageRequest request = PageRequest.of(0, size, by(DESC, "point"));
        Slice<Magazine> magazines = magazineRepository.findAll(request);
        return magazines.map(MagazineLink::new).getContent();
    }


    // private ---------------------------------------------------------------------------------------------------------

    private Magazine find(Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
    }

}
