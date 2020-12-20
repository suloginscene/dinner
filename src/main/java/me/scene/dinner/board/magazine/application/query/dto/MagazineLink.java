package me.scene.dinner.board.magazine.application.query.dto;

import me.scene.dinner.board.common.dto.Link;
import me.scene.dinner.board.magazine.domain.common.Magazine;


public class MagazineLink extends Link {

    public MagazineLink(Magazine magazine) {
        super(magazine);
    }

}
