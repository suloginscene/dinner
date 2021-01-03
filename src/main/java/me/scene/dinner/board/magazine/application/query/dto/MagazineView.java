package me.scene.dinner.board.magazine.application.query.dto;

import lombok.Getter;
import me.scene.dinner.board.common.dto.View;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;
import me.scene.dinner.board.magazine.domain.magazine.model.Type;
import me.scene.dinner.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.dinner.board.magazine.domain.open.model.OpenMagazine;

import java.util.ArrayList;
import java.util.List;


@Getter
public class MagazineView extends View {

    private final String shortExplanation;
    private final String longExplanation;

    private final boolean hasChild;

    private final String policy;
    private final List<String> members = new ArrayList<>();
    private final List<String> writers = new ArrayList<>();


    public MagazineView(Magazine magazine) {
        super(magazine);

        this.shortExplanation = magazine.getShortExplanation();
        this.longExplanation = magazine.getLongExplanation().replace("\n", "<br>");

        this.hasChild = magazine.getTopics().exists();

        this.policy = magazine.type().name();
        if (magazine.type() == Type.MANAGED) {
            ManagedMagazine managed = (ManagedMagazine) magazine;
            members.addAll(managed.memberNames());
        }
        if (magazine.type() == Type.OPEN) {
            OpenMagazine open = (OpenMagazine) magazine;
            writers.addAll(open.writerNames());
        }
    }

}
