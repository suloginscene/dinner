package me.scene.dinner.board.application.topic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.domain.topic.ArticleSummary;
import me.scene.dinner.board.domain.topic.MagazineSummary;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TopicDto {

    private final Long id;

    private final String manager;


    private final String title;

    private final String shortExplanation;

    private final String longExplanation;

    private final MagazineSummary magazine;

    private final List<ArticleSummary> privateArticles;

    private final List<ArticleSummary> publicArticles;

    public boolean isEmpty() {
        return privateArticles.isEmpty() && publicArticles.isEmpty();
    }

}
