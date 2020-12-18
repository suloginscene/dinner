package me.scene.dinner.board.topic.application.command.request;

import lombok.Data;


@Data
public class TopicCreateRequest {

    private final String ownerName;

    private final Long magazineId;

    private final String title;
    private final String shortExplanation;
    private final String longExplanation;

}
