package me.scene.dinner.tag;

import lombok.Data;

import java.util.List;

@Data
public class TagDto {

    private final String name;

    private final List<ArticleSummary> articles;

}
