package me.scene.dinner.board.magazine.application.query.dto;

import lombok.Getter;
import me.scene.dinner.board.common.dto.View;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.Type;
import me.scene.dinner.board.magazine.domain.managed.ManagedMagazine;
import me.scene.dinner.board.magazine.domain.open.OpenMagazine;

import java.util.List;


@Getter
public class MagazineView extends View {

    private final String shortExplanation;
    private final String longExplanation;

    private final boolean hasChild;

    private final String policy;
    private List<String> members;
    private List<String> writers;


    public MagazineView(Magazine magazine) {
        super(magazine);

        this.shortExplanation = magazine.getShortExplanation();
        this.longExplanation = magazine.getLongExplanation();

        this.hasChild = magazine.getTopics().exists();

        this.policy = magazine.type().name();
        if (magazine.type() == Type.MANAGED) {
            this.members = ((ManagedMagazine) magazine).memberNames();
        }
        if (magazine.type() == Type.OPEN) {
            this.writers = ((OpenMagazine) magazine).writerNames();
        }
    }

}
