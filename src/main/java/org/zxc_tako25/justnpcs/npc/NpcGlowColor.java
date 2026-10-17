package org.zxc_tako25.justnpcs.npc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum NpcGlowColor {
    WHITE(NamedTextColor.WHITE),
    RED(NamedTextColor.RED),
    GOLD(NamedTextColor.GOLD),
    YELLOW(NamedTextColor.YELLOW),
    GREEN(NamedTextColor.GREEN),
    AQUA(NamedTextColor.AQUA),
    BLUE(NamedTextColor.BLUE),
    LIGHT_PURPLE(NamedTextColor.LIGHT_PURPLE),
    DARK_RED(NamedTextColor.DARK_RED),
    DARK_GREEN(NamedTextColor.DARK_GREEN),
    DARK_BLUE(NamedTextColor.DARK_BLUE),
    BLACK(NamedTextColor.BLACK);

    private final NamedTextColor color;

    public static NpcGlowColor fromString(String raw) {
        if (raw == null)
            return WHITE;

        try {
            return valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return WHITE;
        }
    }

    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
