package me.scene.dinner.board.domain.topic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ArticleSummary {

    private final Long id;

    private final String writer;

    private final String title;

    private final String content;

    private final LocalDateTime createdAt;

}
