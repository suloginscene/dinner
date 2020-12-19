package me.scene.dinner.board.magazine.domain;

import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.domain.common.Magazine;

import javax.persistence.Entity;
import java.util.function.Predicate;

import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.board.magazine.domain.common.Magazine.Type.EXCLUSIVE;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExclusiveMagazine extends Magazine {

    public ExclusiveMagazine(Owner owner, String title, String shortExplanation, String longExplanation) {
        super(owner, title, shortExplanation, longExplanation);
    }


    @Override
    public Type type() {
        return EXCLUSIVE;
    }


    @Override
    protected Predicate<String> authorize() {
        return username -> owner.is(username);
    }

}
