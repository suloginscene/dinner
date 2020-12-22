package me.scene.dinner.board.common.dto;

import lombok.Data;
import me.scene.dinner.board.common.domain.model.Board;

import java.time.LocalDateTime;


@Data
public abstract class View {

    private final Long id;
    private final String title;

    private final String owner;
    private final Integer point;
    private final LocalDateTime createdAt;

    protected View(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.owner = board.getOwner().name();
        this.point = board.getPoint().get();
        this.createdAt = board.getCreatedAt();
    }

}
