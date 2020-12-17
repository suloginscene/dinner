package me.scene.dinner.board.common;

import lombok.Getter;

import javax.persistence.Embeddable;


@Embeddable
public class Point {

    public static final int READ = 1;
    public static final int LIKE = 3;

    @Getter
    private int point;


    protected void add(int point) {
        this.point += point;
    }

}
