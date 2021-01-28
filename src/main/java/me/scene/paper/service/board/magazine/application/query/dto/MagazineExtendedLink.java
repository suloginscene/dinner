package me.scene.paper.service.board.magazine.application.query.dto;

import lombok.Getter;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;


@Getter
public class MagazineExtendedLink extends MagazineLink {

    private final String owner;
    private final String shortExplanation;

    public MagazineExtendedLink(Magazine magazine) {
        super(magazine);
        owner = magazine.getOwnerName();
        shortExplanation = magazine.getShortExplanation();
    }

}
