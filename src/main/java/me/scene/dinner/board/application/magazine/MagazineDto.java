package me.scene.dinner.board.application.magazine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import me.scene.dinner.board.domain.magazine.TopicSummary;

import java.util.List;

@Getter
public class MagazineDto extends AbstractInformativeMagazineDto {

    private final Long id;

    private final String title;

    @JsonIgnore
    private final String shortExplanation;

    @JsonIgnore
    private final String longExplanation;

    @JsonIgnore
    private final List<String> writers;

    @JsonIgnore
    private final List<TopicSummary> topics;

    public MagazineDto(Long id, String manager, String title, String shortExplanation, String longExplanation,
                       String policy, List<String> members, List<String> writers, List<TopicSummary> topics) {
        super(policy, manager, members);
        this.id = id;
        this.title = title;
        this.shortExplanation = shortExplanation;
        this.longExplanation = longExplanation;
        this.writers = writers;
        this.topics = topics;
    }

}
