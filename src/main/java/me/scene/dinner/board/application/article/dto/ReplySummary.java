package me.scene.dinner.board.application.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter @RequiredArgsConstructor
public class ReplySummary {

    private final Long id;

    private final String writer;

    private final String content;

    private final LocalDateTime createdAt;

}
