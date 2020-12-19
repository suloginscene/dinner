package me.scene.dinner.board.magazine.domain.common;


public class TypeMismatchException extends org.hibernate.TypeMismatchException {

    protected TypeMismatchException(String title, Magazine.Type actual, Magazine.Type expected) {
        super(String.format("%s is %s, not %s", title, actual.name(), expected.name()));
    }

}
