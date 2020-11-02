package me.scene.dinner.domain.board.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.domain.account.domain.Account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Magazine {

    @Id @GeneratedValue
    private Long id;

    private Long managerId;

    private String title;

    private String shortExplanation;

    private String longExplanation;

    private MagazinePolicy magazinePolicy;

    protected Magazine() {
    }

    public static Magazine create(Account current, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        Magazine magazine = new Magazine();
        magazine.managerId = current.getId();
        magazine.title = title;
        magazine.shortExplanation = shortExplanation;
        magazine.longExplanation = longExplanation;
        magazine.magazinePolicy = MagazinePolicy.valueOf(magazinePolicy);
        return magazine;
    }

}