package me.scene.dinner.board.application.article;

import me.scene.dinner.board.application.common.BoardNotFoundException;

public class ArticleNotFoundException extends BoardNotFoundException {

    public ArticleNotFoundException(Long id) {
        super(id.toString());
    }

}
