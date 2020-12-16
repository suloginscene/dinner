package me.scene.dinner.like.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode
@ToString
public class LikedEvent {

    private final String user;
    private final Long articleId;
    private final String articleWriter;
    private final String articleTitle;

}
