package me.scene.paper.common.utility;

import org.springframework.stereotype.Component;


@Component
public class LinkConverter {

    private static final String TEMPLATE = "<a href=\"%s\">%s</a>";


    public String account(String username) {
        return String.format(TEMPLATE, "/@" + username, username);
    }

    public String article(Long id, String title) {
        return String.format(TEMPLATE, "/articles/" + id, title);
    }

    public String magazine(Long id, String title) {
        return String.format(TEMPLATE, "/magazines/" + id, title);
    }

    public String excerpt(Long id, String text) {
        return String.format(TEMPLATE, "/articles/" + id, text);
    }

}
