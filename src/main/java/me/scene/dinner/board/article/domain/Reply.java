package me.scene.dinner.board.article.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Board;
import me.scene.dinner.board.common.Owner;

import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Reply extends Board {

    private String content;

    public Reply(String owner, String content) {
        this.owner = new Owner(owner);
        this.content = content;
    }

}
