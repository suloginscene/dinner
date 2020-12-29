package me.scene.dinner.common.util;


public class LinkConvertUtils {

    public static String account(String username) {
        return String.format("<a href=\"/@%s\">%s</a>", username, username);
    }

    public static String article(Long id, String title) {
        return String.format("<a href=\"/articles/%s\">%s</a>", id, title);
    }

    public static String magazine(Long id, String title) {
        return String.format("<a href=\"/magazines/%s\">%s</a>", id, title);
    }

}
