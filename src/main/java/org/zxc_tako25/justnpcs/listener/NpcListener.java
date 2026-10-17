package org.zxc_tako25.justnpcs.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.zxc_tako25.justnpcs.JustNPCs;

@RequiredArgsConstructor
public final class NpcListener implements Listener {
    private final JustNPCs plugin;

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (plugin.getNpcRegistry().getByEntityId(event.getEntity().getEntityId()) != null)
            event.setCancelled(true);
    }
}
