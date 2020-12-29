package me.scene.dinner.common.notification.message;

import org.springframework.stereotype.Component;


@Component
public class LinkConverter {

    public String account(String username) {
        return String.format("<a href=\"/@%s\">%s</a>", username, username);
    }

    public String article(Long id, String title) {
        return String.format("<a href=\"/articles/%s\">%s</a>", id, title);
    }

    public String magazine(Long id, String title) {
        return String.format("<a href=\"/magazines/%s\">%s</a>", id, title);
    }

}
