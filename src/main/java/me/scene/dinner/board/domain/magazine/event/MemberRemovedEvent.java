package me.scene.dinner.board.domain.magazine.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode
public class MemberRemovedEvent {

    private final Long magazineId;
    private final String magazineTitle;
    private final String member;

}
