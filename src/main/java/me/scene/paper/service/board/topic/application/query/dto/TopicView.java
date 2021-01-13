package me.scene.paper.service.board.topic.application.query.dto;

import lombok.Getter;
import me.scene.paper.service.board.common.dto.View;
import me.scene.paper.service.board.magazine.application.query.dto.MagazineInfo;
import me.scene.paper.service.board.topic.domain.model.Topic;


@Getter
public class TopicView extends View {

    private final String shortExplanation;
    private final String longExplanation;

    private final boolean hasChild;

    private final MagazineInfo magazine;


    public TopicView(Topic topic) {
        super(topic);

        this.shortExplanation = topic.getShortExplanation();
        this.longExplanation = topic.getLongExplanation().replace("\n", "<br>");

        this.hasChild = topic.hasArticle();

        magazine = new MagazineInfo(topic.getMagazine());
    }

}
