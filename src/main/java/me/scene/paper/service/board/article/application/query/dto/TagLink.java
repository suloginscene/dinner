package me.scene.paper.service.board.article.application.query.dto;

import lombok.Getter;
import me.scene.paper.service.board.article.domain.tag.model.Tag;


@Getter
public class TagLink implements Comparable<TagLink> {

    private final String name;
    private final int count;

    public TagLink(Tag tag) {
        this.name = tag.getName();
        this.count = tag.getArticleTags().size();
    }

    @Override
    public int compareTo(TagLink tagLink) {
        return Integer.compare(this.count, tagLink.count);
    }

}
