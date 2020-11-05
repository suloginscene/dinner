package me.scene.dinner.board.topic.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.magazine.domain.Magazine;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Topic {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Magazine magazine;

    private Long managerId;

    private String title;

    private String shortExplanation;

    private String longExplanation;

    protected Topic() {
    }

    public static Topic create(Magazine magazine, Long managerId, String title, String shortExplanation, String longExplanation) {
        Topic topic = new Topic();
        topic.magazine = magazine;
        topic.managerId = managerId;
        topic.title = title;
        topic.shortExplanation = shortExplanation;
        topic.longExplanation = longExplanation;
        return topic;
    }

}
