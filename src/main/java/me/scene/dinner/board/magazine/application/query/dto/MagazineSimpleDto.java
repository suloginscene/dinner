package me.scene.dinner.board.magazine.application.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import me.scene.dinner.board.magazine.domain.common.Magazine;

import java.util.List;

@Getter
public class MagazineSimpleDto {

    private final Long id;

    private final String title;

    private final String policy;

    private final String manager;

    private final List<String> members;

    @JsonIgnore
    private final String shortExplanation;

    @JsonIgnore
    private final String longExplanation;

    @JsonIgnore
    private final List<String> writers;

    @JsonIgnore
    private final boolean hasChild;

    public MagazineSimpleDto(Magazine m) {
        this.id = m.getId();
        this.title = m.getTitle();
        this.manager = m.getOwner().getOwnerName();
        this.shortExplanation = m.getShortExplanation();
        this.longExplanation = m.getLongExplanation();
        this.hasChild = m.hasChild();
        this.policy = m.type();
        this.members = m.memberNames();
        this.writers = m.writerNames();
    }

}
