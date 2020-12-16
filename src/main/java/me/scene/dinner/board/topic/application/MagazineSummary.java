package me.scene.dinner.board.topic.application;

import lombok.Getter;
import me.scene.dinner.board.magazine.application.AbstractInformativeMagazineDto;
import me.scene.dinner.board.magazine.domain.Magazine;

@Getter
public class MagazineSummary extends AbstractInformativeMagazineDto {

    private final Long id;

    private final String title;

    public MagazineSummary(Magazine m) {
        super(m.getPolicy().name(), m.getOwner().getOwnerName(), m.getMembers());
        this.id = m.getId();
        this.title = m.getTitle();
    }

}
