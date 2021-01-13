package me.scene.paper.service.board.topic.application.command.request;

import lombok.Data;


@Data
public class TopicCreateRequest {

    private final String username;

    private final Long magazineId;

    private final String title;
    private final String shortExplanation;
    private final String longExplanation;

}
