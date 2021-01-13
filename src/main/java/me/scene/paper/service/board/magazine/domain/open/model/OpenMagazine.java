package me.scene.paper.service.board.magazine.domain.open.model;

import lombok.NoArgsConstructor;
import me.scene.paper.service.board.magazine.domain.magazine.model.Authorization;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.service.board.magazine.domain.magazine.model.Type;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.paper.service.board.magazine.domain.magazine.model.Type.OPEN;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class OpenMagazine extends Magazine {

    @Transient
    private final Authorization authorization = new Authorization(username -> true);

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "magazine_id")
    private final Set<Writer> writers = new HashSet<>();


    public OpenMagazine(String owner, String title, String shortExplanation, String longExplanation) {
        super(owner, title, shortExplanation, longExplanation);
    }


    @Override
    public Type type() {
        return OPEN;
    }

    @Override
    public Authorization authorization() {
        return authorization;
    }


    public List<String> writerNames() {
        return writers.stream()
                .map(Writer::getName)
                .collect(toList());
    }


    public void logWriting(String name) {
        Optional<Writer> writerByName = findWriterByName(name);
        if (writerByName.isPresent()) {
            Writer writer = writerByName.get();
            writer.write();
        } else {
            Writer writer = new Writer(name);
            writer.write();
            writers.add(writer);
        }
    }

    public void logErasing(String name) {
        Optional<Writer> writerByName = findWriterByName(name);
        if (writerByName.isPresent()) {
            Writer writer = writerByName.get();
            writer.erase();
            if (!writer.hasWriting()) {
                writers.remove(writer);
            }
        }
    }


    private Optional<Writer> findWriterByName(String name) {
        return writers.stream()
                .filter(writer -> writer.is(name))
                .findAny();
    }

}
