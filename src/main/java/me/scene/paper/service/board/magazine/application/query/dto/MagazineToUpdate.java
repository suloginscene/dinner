package me.scene.paper.service.board.magazine.application.query.dto;

import lombok.Getter;
import me.scene.paper.service.board.common.dto.View;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;


@Getter
public class MagazineToUpdate extends View {

    private final String shortExplanation;
    private final String longExplanation;
    private final String policy;


    public MagazineToUpdate(Magazine magazine) {
        super(magazine);

        this.shortExplanation = magazine.getShortExplanation();
        this.longExplanation = magazine.getLongExplanation();
        this.policy = magazine.typeName();
    }

}
