package me.scene.paper.board.magazine.application.query.dto;

import me.scene.paper.board.common.dto.Link;
import me.scene.paper.board.magazine.domain.magazine.model.Magazine;


public class MagazineLink extends Link {

    public MagazineLink(Magazine magazine) {
        super(magazine);
    }

}
