package me.scene.paper.service.board.topic.application.command.request;

import lombok.Data;


@Data
public class TopicUpdateRequest {

    private final String username;

    private final Long id;

    private final String title;
    private final String shortExplanation;
    private final String longExplanation;

}
