package me.scene.dinner.board.topic.application;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class TopicDto {

    private Long id;

    private Long magazineId;

    private String magazine;

    private String manager;

    private String title;

    private String shortExplanation;

    private String longExplanation;

    private TopicDto() {
    }

    public static TopicDto create(Long id, Long magazineId, String magazine, String manager, String title, String shortExplanation, String longExplanation) {
        TopicDto dto = new TopicDto();
        dto.id = id;
        dto.magazineId = magazineId;
        dto.magazine = magazine;
        dto.manager = manager;
        dto.title = title;
        dto.shortExplanation = shortExplanation;
        dto.longExplanation = longExplanation;
        return dto;
    }

}
