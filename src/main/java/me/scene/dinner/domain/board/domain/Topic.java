package me.scene.dinner.domain.board.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

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

    private String title;

    private String shortExplanation;

    private String longExplanation;

}
