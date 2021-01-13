package me.scene.paper.admin.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.common.entity.BaseEntity;

import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Excerpt extends BaseEntity {

    private String excerpt;

    public Excerpt(String excerpt) {
        this.excerpt = excerpt;
    }

}
