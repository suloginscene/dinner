package me.scene.paper.board.magazine.application.command.request;

import lombok.Data;


@Data
public class MagazineUpdateRequest {

    private final String username;

    private final String title;
    private final String shortExplanation;
    private final String longExplanation;

}
