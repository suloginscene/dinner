package me.scene.dinner.board.topic.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TopicSimpleDto {

    private final Long id;
    private final String manager;
    private final String title;
    private final String shortExplanation;
    private final String longExplanation;
    private final MagazineSummary magazine;
    private final int articleCount;

}
