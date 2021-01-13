package me.scene.paper.service.board.magazine.domain.exclusive.model;

import lombok.NoArgsConstructor;
import me.scene.paper.service.board.magazine.domain.magazine.model.Authorization;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.service.board.magazine.domain.magazine.model.Type;

import javax.persistence.Entity;
import javax.persistence.Transient;

import static lombok.AccessLevel.PROTECTED;
import static me.scene.paper.service.board.magazine.domain.magazine.model.Type.EXCLUSIVE;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExclusiveMagazine extends Magazine {

    @Transient
    private final Authorization authorization = new Authorization(username -> owner.is(username));

    public ExclusiveMagazine(String owner, String title, String shortExplanation, String longExplanation) {
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
