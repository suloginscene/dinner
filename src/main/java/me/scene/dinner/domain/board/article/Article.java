package me.scene.dinner.domain.board.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.domain.board.topic.Topic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Article {

    @Id @GeneratedValue
    private Long id;

    private Long writer;

    @ManyToOne
    private Topic topic;

    private String title;

    private String content;

}
