package me.scene.dinner.board.application.magazine;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MagazineBestListCache {

    @Getter
    private final int size = 5;

    @Getter @Setter
    private List<MagazineDto> bestMagazines;

}
