package me.scene.paper.account.domain.account.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;


@Embeddable
@Getter @EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED) @AllArgsConstructor
public class Profile {

    @Column(nullable = false)
    private String greeting;

}
