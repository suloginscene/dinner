package me.scene.dinner.board.magazine.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.List;

@Getter
public class MagazineSimpleDto extends AbstractInformativeMagazineDto {

    private final Long id;

    private final String title;

    @JsonIgnore
    private final String shortExplanation;

    @JsonIgnore
    private final String longExplanation;

    @JsonIgnore
    private final List<String> writers;

    @JsonIgnore
    private final int topicCount;

    public MagazineSimpleDto(Long id, String manager, String title, String shortExplanation, String longExplanation,
                             String policy, List<String> members, List<String> writers, int topicCount) {
        super(policy, manager, members);
        this.id = id;
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
        this.writers = writers;
        this.topicCount = topicCount;
    }

}
