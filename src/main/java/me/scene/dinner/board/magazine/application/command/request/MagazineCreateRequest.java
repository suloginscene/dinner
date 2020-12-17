package me.scene.dinner.board.magazine.application.command.request;

import lombok.Data;


@Data
public class MagazineCreateRequest {

    private final String ownerName;
    private final String title;
    private final String shortExplanation;
    private final String longExplanation;
    private final String type;

}
