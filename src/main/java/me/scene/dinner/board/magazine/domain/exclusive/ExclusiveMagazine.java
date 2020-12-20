package me.scene.dinner.board.magazine.domain.exclusive;

import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.domain.Owner;
import me.scene.dinner.board.magazine.domain.common.Authorization;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.Type;

import javax.persistence.Entity;
import javax.persistence.Transient;

import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.board.magazine.domain.common.Type.EXCLUSIVE;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExclusiveMagazine extends Magazine {

    @Transient
    private final Authorization authorization = new Authorization(username -> owner.is(username));

    public ExclusiveMagazine(Owner owner, String title, String shortExplanation, String longExplanation) {
        super(owner, title, shortExplanation, longExplanation);
    }


    @Override
    public Type type() {
        return EXCLUSIVE;
    }

    @Override
    public Authorization authorization() {
        return authorization;
    }

}
