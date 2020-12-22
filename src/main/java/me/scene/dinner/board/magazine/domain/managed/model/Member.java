package me.scene.dinner.board.magazine.domain.managed.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseEntity {

    private String name;

    protected Member(String name) {
        this.name = name;
    }

    protected boolean is(String username) {
        return name.equals(username);
    }

}
