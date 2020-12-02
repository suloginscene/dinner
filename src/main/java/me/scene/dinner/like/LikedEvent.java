package me.scene.dinner.like;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode
public class LikedEvent {

    private final String writer;
    private final String user;
    private final String title;

}
