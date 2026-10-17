package org.zxc_tako25.justnpcs.npc;

import java.util.Locale;

public enum NpcType {
    PLAYER,
    VILLAGER;

    public static NpcType fromString(String raw) {
        if (raw == null)
            return null;

        try {
            return valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public boolean isPlayer() {
        return this == PLAYER;
    }

    public boolean isVillager() {
        return this == VILLAGER;
    }
}
