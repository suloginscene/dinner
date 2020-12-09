package me.scene.dinner.tag;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Tag {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    protected Tag() {
    }

    public static Tag create(String name) {
        Tag tag = new Tag();
        tag.name = name;
        return tag;
    }

}
