package me.scene.dinner.board.topic.application;

import lombok.Getter;
import me.scene.dinner.board.magazine.domain.common.Magazine;

@Getter
public class MagazineSummary {

    private final Long id;

    private final String title;

    public MagazineSummary(Magazine m) {
        this.id = m.getId();
        this.title = m.getTitle();
    }

}
