package org.zxc_tako25.justnpcs.visibility;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.utils.LocationUtils;

@RequiredArgsConstructor
public final class VisibilityManager implements VisibilityFilter {
    private final JustNPCs plugin;

    @Override
    public boolean canSee(Player player, Npc npc) {
        if (player == null || npc == null)
            return false;
        if (npc.getLocation() == null || npc.getLocation().getWorld() == null)
            return false;
        if (!LocationUtils.isSameWorld(player.getLocation(), npc.getLocation()))
            return false;

        return LocationUtils.isWithinDistance(
                player.getLocation(),
                npc.getLocation(),
                plugin.getConfigManager().viewDistance());
    }
}
