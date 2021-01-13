package me.scene.paper.service.board.magazine.application.query.dto;

import lombok.Getter;
import me.scene.paper.service.board.common.dto.View;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;

import java.util.List;


@Getter
public class MagazineView extends View {

    private final String shortExplanation;
    private final String longExplanation;

    private final boolean hasChild;

    private final String policy;
    private final List<String> members;
    private final List<String> writers;


    public MagazineView(Magazine magazine) {
        super(magazine);

        this.shortExplanation = magazine.getShortExplanation();
        this.longExplanation = magazine.getLongExplanation().replace("\n", "<br>");

        this.hasChild = magazine.hasTopic();

        this.policy = magazine.typeName();
        this.members = magazine.memberNames();
        this.writers = magazine.writerNames();
    }

}
