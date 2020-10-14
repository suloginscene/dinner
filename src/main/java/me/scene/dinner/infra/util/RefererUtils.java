package me.scene.dinner.infra.util;

import me.scene.dinner.domain.account.AccountController;

import javax.servlet.http.HttpServletRequest;

public class RefererUtils {

    public static String parse(HttpServletRequest request, String url) {
        String referer = request.getHeader("Referer");

        if (referer == null) return null;
        if (externalReference(referer, url)) return "external";

        int rootIndex = url.length();
        return referer.substring(rootIndex);
    }

    private static boolean externalReference(String referer, String url) {
        return !referer.startsWith(url);
    }

    public static boolean contextless(String referer) {
        return referer == null
                || referer.equals("external")
                || referer.startsWith(AccountController.URL_LOGIN)
                || referer.startsWith(AccountController.URL_VERIFY)
                || referer.startsWith(AccountController.URL_SIGNUP);
    }

}