package me.scene.paper.board.magazine.domain.open.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.paper.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class Writer extends BaseEntity {

    @Getter @Column(length = 16, nullable = false)
    private String name;

    private int count;


    public Writer(String name) {
        this.name = name;
    }


    protected boolean is(String username) {
        return name.equals(username);
    }

    protected void write() {
        count++;
    }

    protected void erase() {
        count--;
    }

    protected boolean hasWriting() {
        return count > 0;
    }

}
