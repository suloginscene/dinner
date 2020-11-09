package me.scene.dinner.board.article.application;

public class PublicationException extends IllegalStateException {
    public PublicationException(Long id) {
        super(id.toString());
    }
}
