package org.zxc_tako25.justnpcs.packet;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.utils.VersionUtils;

public final class MetadataResolver {
    private final JustNPCs plugin;
    private final int playerSkinPartsIndex;
    private final int villagerDataIndex;
    private final int armorStandFlagsIndex;

    public MetadataResolver(JustNPCs plugin) {
        this.plugin = plugin;
        this.playerSkinPartsIndex = resolvePlayerSkinParts();
        this.villagerDataIndex = resolveVillagerData();
        this.armorStandFlagsIndex = resolveArmorStandFlags();
    }

    public int playerSkinPartsIndex() {
        return playerSkinPartsIndex;
    }

    public int villagerDataIndex() {
        return villagerDataIndex;
    }

    public int armorStandFlagsIndex() {
        return armorStandFlagsIndex;
    }

    private int resolvePlayerSkinParts() {
        return VersionUtils.atLeast(ServerVersion.V_1_17) ? 17 : 16;
    }

    private int resolveVillagerData() {
        int override = plugin.getConfigManager().metadataOverride("villager-data-index");
        if (override >= 0)
            return override;
        return VersionUtils.atLeast(ServerVersion.V_1_17) ? 18 : 17;
    }

    private int resolveArmorStandFlags() {
        int override = plugin.getConfigManager().metadataOverride("armor-stand-flags-index");
        if (override >= 0)
            return override;
        return VersionUtils.atLeast(ServerVersion.V_1_17) ? 15 : 14;
    }
}
