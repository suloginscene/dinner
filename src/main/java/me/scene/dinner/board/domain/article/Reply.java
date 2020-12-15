package me.scene.dinner.board.domain.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.domain.common.Board;
import me.scene.dinner.board.domain.common.Owner;

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
