package org.zxc_tako25.justnpcs.npc;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public enum NpcProfession {
    NONE,
    ARMORER,
    BUTCHER,
    CARTOGRAPHER,
    CLERIC,
    FARMER,
    FISHERMAN,
    FLETCHER,
    LEATHERWORKER,
    LIBRARIAN,
    MASON,
    NITWIT,
    SHEPHERD,
    TOOLSMITH,
    WEAPONSMITH;

    public static NpcProfession fromString(String raw) {
        if (raw == null)
            return NONE;

        String key = raw.trim().toUpperCase(Locale.ROOT);
        if (key.startsWith("MINECRAFT:"))
            key = key.substring("MINECRAFT:".length());
        if (key.equals("STONE_MASON"))
            key = "MASON";

        try {
            return valueOf(key);
        } catch (IllegalArgumentException ex) {
            return NONE;
        }
    }

    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
