package me.scene.paper.service.board.common.dto;

import lombok.Data;
import me.scene.paper.service.board.common.domain.model.Board;


@Data
public abstract class Link {

    private final Long id;
    private final String title;

    protected Link(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
    }

}
