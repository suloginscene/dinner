package me.scene.dinner.infra.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yy.MM.dd.");

    public static String format(LocalDateTime localDateTime) {
        return simpleFormatter.format(localDateTime);
    }

}
