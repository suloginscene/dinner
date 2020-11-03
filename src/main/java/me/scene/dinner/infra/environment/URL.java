package me.scene.dinner.infra.environment;

public class URL {

    private final String url;

    public URL(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }

}
