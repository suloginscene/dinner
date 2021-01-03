package me.scene.paper.board.article.ui.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;


@Component
@RequiredArgsConstructor
public class TagParser {

    private final ObjectMapper objectMapper;


    public Set<String> parse(String jsonTags) {
        if (StringUtils.isEmpty(jsonTags)) return new HashSet<>();
        try {
            Set<TagForm> tags = objectMapper.readValue(jsonTags, TagForm.TYPE);
            return tags.stream().map(TagForm::getValue).collect(toSet());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot parse json for tag: " + e.getMessage());
        }
    }

}
