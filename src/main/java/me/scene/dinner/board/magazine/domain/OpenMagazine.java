package me.scene.dinner.board.magazine.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.board.common.Owner;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.board.magazine.domain.common.Type.OPEN;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class OpenMagazine extends Magazine {

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "magazine_id")
    private final Set<Writer> writers = new HashSet<>();


    public OpenMagazine(Owner owner, String title, String shortExplanation, String longExplanation) {
        super(owner, title, shortExplanation, longExplanation);
    }


    @Override
    public String type() {
        return OPEN.name();
    }

    @Override
    public List<String> writerNames() {
        return writers.stream()
                .map(Writer::getName)
                .collect(toList());
    }

    @Override
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

    @Override
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


    @Entity @NoArgsConstructor(access = PROTECTED)
    public static class Writer extends BaseEntity {

        @Getter
        private String name;
        private int count;

        public Writer(String name) {
            this.name = name;
        }

        private boolean is(String username) {
            return name.equals(username);
        }

        private void write() {
            count++;
        }

        private void erase() {
            count--;
        }

        private boolean hasWriting() {
            return count > 0;
        }

    }

}
