package me.scene.dinner.board.magazine.domain.open;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class Writer extends BaseEntity {

    @Getter
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
