package org.zxc_tako25.justnpcs.npc;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@Setter
public final class Npc {
    private final String id;
    private final int entityId;
    private final UUID uuid;
    private final NpcType type;
    private Location location;
    private NpcSkin skin;
    private final NpcHologram hologram;
    private final NpcAction action;
    private boolean glow;
    private NpcGlowColor glowColor;
    private NpcProfession profession;

    public Npc(String id, int entityId, Location location, NpcType type) {
        this.id = id;
        this.entityId = entityId;
        this.uuid = UUID.randomUUID();
        this.location = location;
        this.type = type;
        this.skin = new NpcSkin(null, null);
        this.hologram = NpcHologram.empty();
        this.action = NpcAction.empty();
        this.glow = false;
        this.glowColor = NpcGlowColor.WHITE;
        this.profession = NpcProfession.NONE;
    }

    public boolean isPlayerType() {
        return type.isPlayer();
    }

    public boolean isVillagerType() {
        return type.isVillager();
    }
}
