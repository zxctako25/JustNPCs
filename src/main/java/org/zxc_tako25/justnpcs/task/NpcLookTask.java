package org.zxc_tako25.justnpcs.task;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.npc.Npc;

@RequiredArgsConstructor
public final class NpcLookTask extends BukkitRunnable {
    private static final double LOOK_DISTANCE = 8.0;
    private final JustNPCs plugin;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Npc npc : plugin.getNpcRegistry().all()) {
                Location npcLocation = npc.getLocation();
                if (npcLocation == null || npcLocation.getWorld() == null)
                    continue;
                if (!npcLocation.getWorld().equals(player.getWorld()))
                    continue;
                if (npcLocation.distanceSquared(player.getLocation()) > LOOK_DISTANCE * LOOK_DISTANCE)
                    continue;

                Location playerLocation = player.getLocation();
                double dx = playerLocation.getX() - npcLocation.getX();
                double dy = (playerLocation.getY() + player.getEyeHeight()) - (npcLocation.getY() + 1.62);
                double dz = playerLocation.getZ() - npcLocation.getZ();
                double distanceXZ = Math.sqrt(dx * dx + dz * dz);
                if (distanceXZ < 0.01)
                    continue;

                float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
                float pitch = (float) Math.toDegrees(-Math.atan2(dy, distanceXZ));

                plugin.getNpcSpawner().lookAt(player, npc, yaw, pitch);
            }
        }
    }
}
