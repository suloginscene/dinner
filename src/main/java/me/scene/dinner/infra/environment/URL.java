package me.scene.dinner.infra.environment;

public class URL {

    private final String url;

    public URL(String url) {
        this.url = url;
    }

    public String get() {
        return url;
    }

}
