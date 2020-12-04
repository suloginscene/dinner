package me.scene.dinner.board.domain.article;

public enum RatingType {

    READ(1), LIKE(3), DISLIKE(-3);


    private final int point;

    RatingType(int point) {
        this.point = point;
    }

    public int point() {
        return point;
    }

}
