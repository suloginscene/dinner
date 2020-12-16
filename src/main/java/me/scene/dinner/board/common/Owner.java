package me.scene.dinner.board.common;

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

    private String ownerName;

    public boolean is(String name) {
        return ownerName.equals(name);
    }

    public void identify(String name) {
        if (this.is(name)) return;
        throw new NotOwnerException(name);
    }

}
