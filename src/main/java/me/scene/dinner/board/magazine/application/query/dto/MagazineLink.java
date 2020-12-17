package me.scene.dinner.board.magazine.application.query.dto;

import lombok.Data;
import me.scene.dinner.board.magazine.domain.common.Magazine;


@Data
public class MagazineLink {

    private final Long id;
    private final String title;

    public MagazineLink(Magazine magazine) {
        this.id = magazine.getId();
        this.title = magazine.getTitle();
    }

}
