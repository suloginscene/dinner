package me.scene.dinner.like.domain;

import lombok.Data;


@Data
public class LikedEvent {

    private final String user;
    private final Long articleId;
    private final String articleWriter;
    private final String articleTitle;

}
