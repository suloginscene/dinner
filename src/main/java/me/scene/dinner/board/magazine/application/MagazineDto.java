package me.scene.dinner.board.magazine.application;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.topic.application.TopicDto;
import me.scene.dinner.board.topic.domain.Topic;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public class MagazineDto {

    private Long id;

    private String manager;

    private List<String> writers;

    private String title;

    private String shortExplanation;

    private String longExplanation;

    private String magazinePolicy;

    private List<TopicDto> topicDtoList;

    private MagazineDto() {
    }

    public static MagazineDto create(Long id, String manager, List<String> writers,
                                     String title, String shortExplanation, String longExplanation,
                                     String magazinePolicy, List<Topic> topics, List<String> topicManagers) {
        MagazineDto dto = new MagazineDto();
        dto.id = id;
        dto.manager = manager;
        dto.writers = writers;
        dto.title = title;
        dto.shortExplanation = shortExplanation;
        dto.longExplanation = longExplanation;
        dto.magazinePolicy = magazinePolicy;

        // TODO
        int size = topics.size();
        if (size != topicManagers.size()) throw new IllegalStateException();

        List<TopicDto> topicDtoList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Topic topic = topics.get(i);

            List<Article> articles = topic.getArticles();
            int s = articles.size();
            List<String> tests = new ArrayList<>();
            for (int j = 0; j < s; j++) {
                tests.add("테스트");
            }

            TopicDto topicDto = TopicDto.create(topic.getId(), id, title, topicManagers.get(i),
                    topic.getTitle(), topic.getShortExplanation(), topic.getLongExplanation(),
                    articles, tests);
            topicDtoList.add(topicDto);
        }

        dto.topicDtoList = topicDtoList;
        return dto;
    }

}
