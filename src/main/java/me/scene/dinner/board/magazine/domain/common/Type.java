package me.scene.dinner.board.magazine.domain.common;


public enum Type {

    OPEN, MANAGED, EXCLUSIVE;

    public void check(Type expected) {
        if (this == expected) return;
        throw new TypeMismatchException(expected, this);
    }

}
