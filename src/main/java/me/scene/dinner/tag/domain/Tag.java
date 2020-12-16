package me.scene.dinner.tag.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Tag extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "tag_id")
    private final Set<TaggedArticle> taggedArticles = new HashSet<>();


    public Tag(String name) {
        this.name = name;
    }

    public void addTaggedArticle(TaggedArticle taggedArticle) {
        taggedArticles.add(taggedArticle);
    }

    public Set<TaggedArticle> getPublicizedTaggedArticles() {
        return taggedArticles.stream().filter(TaggedArticle::isPublicized).collect(Collectors.toSet());
    }

}
