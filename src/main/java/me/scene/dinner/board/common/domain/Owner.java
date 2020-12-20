package me.scene.dinner.board.common.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;


@Embeddable
@Getter @EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED) @AllArgsConstructor
public class Owner {

    private String name;

    public boolean is(String name) {
        return this.name.equals(name);
    }

    public void identify(String name) {
        if (this.is(name)) return;
        throw new NotOwnerException(name);
    }

}
