package me.scene.dinner.board.magazine.domain;

import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.domain.common.AccessException;
import me.scene.dinner.board.magazine.domain.common.Magazine;

import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.board.magazine.domain.common.Type.EXCLUSIVE;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExclusiveMagazine extends Magazine {

    public ExclusiveMagazine(Owner owner, String title, String shortExplanation, String longExplanation) {
        super(owner, title, shortExplanation, longExplanation);
    }


    @Override
    public String type() {
        return EXCLUSIVE.name();
    }

    @Override
    public void checkAuthorization(String username) {
        if (owner.is(username)) return;
        throw new AccessException(username, getTitle(), EXCLUSIVE);
    }

}
