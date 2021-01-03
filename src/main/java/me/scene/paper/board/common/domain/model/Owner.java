package me.scene.paper.board.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.scene.paper.board.common.domain.exception.NotOwnerException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;


@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED) @AllArgsConstructor
public class Owner {

    @Column(length = 16, nullable = false)
    private String name;

    public String name() {
        return name;
    }

    public boolean is(String name) {
        return this.name.equals(name);
    }

    public void identify(String name) {
        if (this.is(name)) return;
        throw new NotOwnerException(name);
    }

}
