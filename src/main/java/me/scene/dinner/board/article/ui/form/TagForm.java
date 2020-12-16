package me.scene.dinner.board.article.ui.form;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class TagForm {

    private String value;

    public static TagFormSetTypeReference TYPE = new TagFormSetTypeReference();

    private static class TagFormSetTypeReference extends TypeReference<Set<TagForm>> {
    }

}
