package me.scene.dinner.board.article.application;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter @ToString
public class ArticleDto {

    private Long id;

    private Long topicId;

    private String topic;

    private String writer;

    private String title;

    private String content;

    private String createAt;

    private ArticleDto() {
    }

    public static ArticleDto create(Long id, Long topicId, String topic, String writer, String title, String content, LocalDateTime localDateTime) {
        ArticleDto dto = new ArticleDto();
        dto.id = id;
        dto.topicId = topicId;
        dto.topic = topic;
        dto.writer = writer;
        dto.title = title;
        dto.content = content;
        dto.createAt = DateTimeFormatter.BASIC_ISO_DATE.format(localDateTime);
        return dto;
    }

}
