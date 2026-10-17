package org.zxc_tako25.justnpcs.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private ColorUtils() {
    }

    public static String color(String message) {
        if (message == null || message.isEmpty())
            return "";

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder builder = new StringBuilder();

        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(builder, ChatColor.of("#" + hex).toString());
        }
        matcher.appendTail(builder);

        return ChatColor.translateAlternateColorCodes('&', builder.toString());
    }

    public static String strip(String message) {
        if (message == null || message.isEmpty())
            return "";
        return ChatColor.stripColor(color(message));
    }
}
