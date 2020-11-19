package me.scene.dinner.account.domain.account;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable @Getter @EqualsAndHashCode
public class Profile {

    private String shortIntroduction;

    protected Profile() {
    }

    public Profile(String shortIntroduction) {
        this.shortIntroduction = shortIntroduction;
    }

}
