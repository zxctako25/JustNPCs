package org.zxc_tako25.justnpcs.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.npc.Npc;

@RequiredArgsConstructor
public final class PlayerConnectionListener implements Listener {
    private final JustNPCs plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getNpcManager().forget(player);

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (!player.isOnline())
                return;

            for (Npc npc : plugin.getNpcRegistry().all())
                if (plugin.getVisibilityManager().canSee(player, npc))
                    plugin.getNpcManager().show(player, npc);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getNpcManager().forget(event.getPlayer());
    }
}
