package me.scene.dinner.board.domain.topic;

import lombok.Getter;
import me.scene.dinner.board.application.magazine.AbstractInformativeMagazineDto;

import java.util.List;

@Getter
public class MagazineSummary extends AbstractInformativeMagazineDto {

    private final Long id;

    private final String title;

    public MagazineSummary(Long id, String manager, String title, String policy, List<String> members) {
        super(policy, manager, members);
        this.id = id;
        this.title = title;
    }

}
