package me.scene.dinner.board.ui.article.tag;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ParsedTagObjectSetTypeReference extends TypeReference<Set<ParsedTagObject>> {

}
