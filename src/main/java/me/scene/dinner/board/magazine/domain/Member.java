package me.scene.dinner.board.magazine.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @EqualsAndHashCode
public class Member {

    private String username;
    private String email;

    protected Member() {
    }

    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
