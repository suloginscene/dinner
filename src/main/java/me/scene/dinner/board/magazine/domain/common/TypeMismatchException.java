package me.scene.dinner.board.magazine.domain.common;


public class TypeMismatchException extends org.hibernate.TypeMismatchException {

    protected TypeMismatchException(Type expected, Type actual) {
        super(String.format("expected: %s, actual: %s", expected.name(), actual.name()));
    }

}
