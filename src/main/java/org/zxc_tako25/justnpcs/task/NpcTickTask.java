package org.zxc_tako25.justnpcs.task;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.npc.Npc;

@RequiredArgsConstructor
public final class NpcTickTask extends BukkitRunnable {
    private final JustNPCs plugin;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers())
            for (Npc npc : plugin.getNpcRegistry().all())
                if (plugin.getVisibilityManager().canSee(player, npc))
                    plugin.getNpcManager().show(player, npc);
                else
                    plugin.getNpcManager().hide(player, npc);
    }
}
