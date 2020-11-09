package me.scene.dinner.board.article.application;

import me.scene.dinner.common.exception.BoardNotFoundException;

public class ArticleNotFoundException extends BoardNotFoundException {

    public ArticleNotFoundException(Long id) {
        super(id.toString());
    }

}
