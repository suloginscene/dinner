package me.scene.dinner.like;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode
public class LikedEvent {

    private final String username;
    private final Long articleId;

}
