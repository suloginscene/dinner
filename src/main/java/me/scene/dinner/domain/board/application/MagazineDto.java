package me.scene.dinner.domain.board.application;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class MagazineDto {

    private String manager;

    private List<String> writers;

    private String title;

    private String shortExplanation;

    private String longExplanation;

    private String magazinePolicy;

    private MagazineDto() {
    }

    public static MagazineDto create(String manager, List<String> writers, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
        MagazineDto dto = new MagazineDto();
        dto.manager = manager;
        dto.writers = writers;
        dto.title = title;
        dto.shortExplanation = shortExplanation;
        dto.longExplanation = longExplanation;
        dto.magazinePolicy = magazinePolicy;
        return dto;
    }

}
