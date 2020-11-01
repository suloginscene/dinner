package me.scene.dinner.infra.util;

public class UrlUtils {

    private static final String FORM = "/board-form";
    private static final String PREFIX = "/board";

    private static final String MAGAZINE_FORM = FORM;
    private static final String TOPIC_FORM = FORM + "/%s";
    private static final String ARTICLE_FORM = FORM + "/%s" + "/%s";

    private static final String MAGAZINE_POST = PREFIX;
    private static final String TOPIC_POST = PREFIX + "/%s";
    private static final String ARTICLE_POST = PREFIX + "/%s" + "/%s";

    private static final String MAGAZINE_READ = PREFIX + "/%s";
    private static final String TOPIC_READ = PREFIX + "/%s" + "/%s";
    private static final String ARTICLE_READ = PREFIX + "/%s" + "/%s" + "/%s";


    public static String read(String magazine) {
        return String.format(MAGAZINE_READ, magazine);
    }

    public static String read(String magazine, String topic) {
        return String.format(TOPIC_READ, magazine, topic);
    }

    public static String read(String magazine, String topic, String article) {
        return String.format(ARTICLE_READ, magazine, topic, article);
    }


    public static String post() {
        return MAGAZINE_POST;
    }

    public static String post(String magazine) {
        return String.format(TOPIC_POST, magazine);
    }

    public static String post(String magazine, String topic) {
        return String.format(ARTICLE_POST, magazine, topic);
    }


    public static String form() {
        return MAGAZINE_FORM;
    }

    public static String form(String magazine) {
        return String.format(TOPIC_FORM, magazine);
    }

    public static String form(String magazine, String topic) {
        return String.format(ARTICLE_FORM, magazine, topic);
    }

}
