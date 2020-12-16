package me.scene.dinner.board.magazine.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Writer extends BaseEntity {

    private String writerName;
    private int writeCount;

    public Writer(String writer) {
        this.writerName = writer;
        writeCount = 1;
    }

    public void write() {
        writeCount++;
    }

    public void erase() {
        writeCount--;
    }

}
