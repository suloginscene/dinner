package me.scene.dinner.board.topic.application.query.dto;

import lombok.Getter;
import me.scene.dinner.board.common.dto.View;
import me.scene.dinner.board.magazine.application.query.dto.MagazineInfo;
import me.scene.dinner.board.topic.domain.model.Topic;


@Getter
public class TopicView extends View {

    private final String shortExplanation;
    private final String longExplanation;

    private final boolean hasChild;

    private final MagazineInfo magazine;


    public TopicView(Topic topic) {
        super(topic);

        this.shortExplanation = topic.getShortExplanation();
        this.longExplanation = topic.getLongExplanation();

        this.hasChild = topic.getArticles().exists();

        magazine = new MagazineInfo(topic.getMagazine());
    }

}
