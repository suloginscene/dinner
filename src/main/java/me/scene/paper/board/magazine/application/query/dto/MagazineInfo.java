package me.scene.paper.board.magazine.application.query.dto;

import lombok.Data;
import me.scene.paper.board.magazine.domain.magazine.model.Magazine;

import java.util.List;


@Data
public class MagazineInfo {

    private final Long id;
    private final String title;
    private final String policy;
    private final String owner;
    private final List<String> members;


    public MagazineInfo(Magazine magazine) {
        id = magazine.getId();
        title = magazine.getTitle();
        policy = magazine.typeName();
        owner = magazine.getOwner().name();
        members = magazine.memberNames();
    }

}
