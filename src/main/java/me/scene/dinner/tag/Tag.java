package me.scene.dinner.tag;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Tag {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "tag_id")
    private final Set<TaggedArticle> taggedArticles = new HashSet<>();

    protected Tag() {
    }

    public static Tag create(String name) {
        Tag tag = new Tag();
        tag.name = name;
        return tag;
    }

    public void addTaggedArticle(TaggedArticle taggedArticle) {
        taggedArticles.add(taggedArticle);
    }

    public Set<TaggedArticle> getPublicizedTaggedArticles() {
        return taggedArticles.stream().filter(TaggedArticle::isPublicized).collect(Collectors.toSet());
    }

}
