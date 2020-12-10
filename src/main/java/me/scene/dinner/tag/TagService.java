package me.scene.dinner.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<String> findAll() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(Tag::getName).collect(toList());
    }

    @Transactional
    public void save(String name) {
        if (tagRepository.existsByName(name)) return;
        tagRepository.save(Tag.create(name));
    }

    @Transactional
    public void delete(String name) {
        Tag tag = tagRepository.findByName(name).orElse(null);
        if (tag == null) return;
        // TODO check deletable
        tagRepository.delete(tag);
    }

}
