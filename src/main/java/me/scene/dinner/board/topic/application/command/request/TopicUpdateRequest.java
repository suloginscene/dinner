package me.scene.dinner.board.topic.application.command.request;

import lombok.Data;


@Data
public class TopicUpdateRequest {

    private final String currentUsername;

    private final Long magazineId;

    private final String title;
    private final String shortExplanation;
    private final String longExplanation;

}
