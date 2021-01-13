package me.scene.paper.service.board.common.domain.model;

import javax.persistence.Embeddable;


@Embeddable
public class Point implements Comparable<Point> {

    public static final int READ = 1;
    public static final int LIKE = 3;

    private int point;


    public int get() {
        return point;
    }

    public void add(int p) {
        point += p;
    }

    @Override
    public int compareTo(Point another) {
        return Integer.compare(this.point, another.point);
    }

}
