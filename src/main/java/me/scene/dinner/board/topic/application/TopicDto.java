package me.scene.dinner.board.topic.application;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.scene.dinner.board.article.application.ArticleDto;
import me.scene.dinner.board.article.domain.Article;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public class TopicDto {

    private Long id;

    private Long magazineId;

    private String magazine;

    private String manager;

    private String title;

    private String shortExplanation;

    private String longExplanation;

    private List<ArticleDto> articleDtoList;

    private TopicDto() {
    }

    public static TopicDto create(Long id, Long magazineId, String magazine, String manager, String title, String shortExplanation, String longExplanation,
                                  List<Article> articles, List<String> articleWriters) {
        TopicDto dto = new TopicDto();
        dto.id = id;
        dto.magazineId = magazineId;
        dto.magazine = magazine;
        dto.manager = manager;
        dto.title = title;
        dto.shortExplanation = shortExplanation;
        dto.longExplanation = longExplanation;

        int size = articles.size();
        if (size != articleWriters.size()) throw new IllegalStateException();

        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Article article = articles.get(i);
            ArticleDto articleDto = ArticleDto.create(article.getId(), id, title, articleWriters.get(i),
                    article.getTitle(), article.getContent(), article.getLocalDateTime());
            articleDtoList.add(articleDto);
        }

        dto.articleDtoList = articleDtoList;
        return dto;
    }

}
