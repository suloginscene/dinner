package me.scene.dinner.domain.board.magazine;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Magazine {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String shortExplanation;

    private String longExplanation;

    private Policy policy;

}
