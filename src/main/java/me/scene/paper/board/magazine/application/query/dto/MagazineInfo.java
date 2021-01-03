package me.scene.paper.board.magazine.application.query.dto;

import lombok.Data;
import me.scene.paper.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.board.magazine.domain.magazine.model.Type;
import me.scene.paper.board.magazine.domain.managed.model.ManagedMagazine;

import java.util.ArrayList;
import java.util.List;

@Data
public class MagazineInfo {

    private final Long id;
    private final String title;

    private final String policy;
    private final String owner;
    private final List<String> members = new ArrayList<>();


    public MagazineInfo(Magazine magazine) {
        id = magazine.getId();
        title = magazine.getTitle();
        policy = magazine.type().name();
        owner = magazine.getOwner().name();
        if (magazine.type() == Type.MANAGED) {
            ManagedMagazine managed = (ManagedMagazine) magazine;
            members.addAll(managed.memberNames());
        }
    }

}
