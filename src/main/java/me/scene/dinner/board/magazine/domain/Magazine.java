package me.scene.dinner.board.magazine.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.board.topic.domain.Topic;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Magazine {

    @Id @GeneratedValue
    private Long id;

    private String manager;

    @ElementCollection(fetch = EAGER)
    private final List<String> writers = new ArrayList<>();

    private String title;

    private String shortExplanation;

    private String longExplanation;

    private MagazinePolicy magazinePolicy;

    @OneToMany(mappedBy = "magazine")
    private final List<Topic> topics = new ArrayList<>();


    protected Magazine() {
    }

    public static Magazine create(String manager, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Magazine magazine = new Magazine();
        magazine.manager = manager;
        magazine.title = title;
        magazine.shortExplanation = shortExplanation;
        magazine.longExplanation = longExplanation;
        magazine.magazinePolicy = MagazinePolicy.valueOf(magazinePolicy);
        return magazine;
    }

    public void add(Topic topic) {
        topics.add(topic);
    }

}
