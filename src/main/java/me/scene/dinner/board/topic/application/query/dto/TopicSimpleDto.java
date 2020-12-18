package me.scene.dinner.board.topic.application.query.dto;

import lombok.Data;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import me.scene.dinner.board.topic.domain.Topic;


@Data
public class TopicSimpleDto {

    private final Long id;
    private final String manager;
    private final String title;
    private final String shortExplanation;
    private final String longExplanation;
    private final int articleCount;
    private final MagazineLink magazine;

    public TopicSimpleDto(Topic t) {
        this.id = t.getId();
        this.manager = t.getOwner().getOwnerName();
        this.title = t.getTitle();
        this.shortExplanation = t.getShortExplanation();
        this.longExplanation = t.getLongExplanation();
        this.articleCount = getArticleCount();
        this.magazine = new MagazineLink(t.getMagazine());
    }

}
