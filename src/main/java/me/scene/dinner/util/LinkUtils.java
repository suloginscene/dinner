package me.scene.dinner.util;

public class LinkUtils {

    public static String accountLink(String username) {
        return String.format("<a href=\"/@%s\">%s</a>", username, username);
    }

    public static String articleLink(Long id, String title) {
        return String.format("<a href=\"/articles/%s\">%s</a>", id, title);
    }

    public static String magazineLink(Long id, String title) {
        return String.format("<a href=\"/magazines/%s\">%s</a>", id, title);
    }

}
