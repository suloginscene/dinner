package me.scene.dinner.domain.board.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.domain.account.domain.Account;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Magazine {

    @Id @GeneratedValue
    private Long id;

    private Long managerId;

    @ElementCollection(fetch = EAGER)
    private final List<Long> writerIds = new ArrayList<>();

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
