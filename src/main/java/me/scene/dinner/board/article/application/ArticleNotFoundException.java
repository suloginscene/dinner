package me.scene.dinner.board.article.application;

import me.scene.dinner.board.common.exception.BoardNotFoundException;

public class ArticleNotFoundException extends BoardNotFoundException {

    public ArticleNotFoundException(Long id) {
        super(id.toString());
    }

}
