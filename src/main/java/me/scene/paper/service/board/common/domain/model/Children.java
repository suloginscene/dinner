package me.scene.paper.service.board.common.domain.model;

import me.scene.paper.service.board.common.domain.exception.NotDeletableException;

import javax.persistence.Embeddable;


@Embeddable
public class Children {

    private int count;

    public void add() {
        count++;
    }

    public void remove() {
        count--;
    }

    public boolean exists() {
        return count != 0;
    }

    public void emptyCheck() {
        if (count == 0) return;
        throw new NotDeletableException();
    }

}
