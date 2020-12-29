package me.scene.dinner.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class RefererParser {

    @Value("${dinner.url}")
    private String url;


    public boolean isMeaningful(String referer) {
        if (referer == null) return false;
        if (!referer.startsWith(url)) return false;
        String ref = referer.substring(url.length());
        return !(
                ref.equals("/") || ref.equals("/login") || ref.equals("/login?error")
                        || ref.equals("/signup") || ref.equals("/forgot") || ref.startsWith("/verify?")
        );
    }

}
