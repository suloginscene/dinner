package me.scene.dinner.board.domain.magazine.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode
public class MagazineChangedEvent {

    private final Long id;

}
