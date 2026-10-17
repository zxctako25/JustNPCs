package org.zxc_tako25.justnpcs.manager;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.npc.Npc;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public final class NpcManager {
    private final JustNPCs plugin;
    private final Map<UUID, Set<String>> visibleNpcs = new ConcurrentHashMap<>();

    public void show(Player player, Npc npc) {
        if (player == null || !player.isOnline() || npc == null)
            return;

        Set<String> visible = visibleNpcs.computeIfAbsent(player.getUniqueId(), key -> ConcurrentHashMap.newKeySet());
        if (!visible.add(npc.getId()))
            return;

        plugin.getNpcSpawner().spawn(player, npc);
    }

    public void hide(Player player, Npc npc) {
        if (player == null || !player.isOnline() || npc == null)
            return;

        Set<String> visible = visibleNpcs.get(player.getUniqueId());
        if (visible == null)
            return;
        if (!visible.remove(npc.getId()))
            return;

        plugin.getNpcSpawner().destroy(player, npc);
    }

    public void refresh(Player player, Npc npc) {
        if (player == null || !player.isOnline() || npc == null)
            return;

        Set<String> visible = visibleNpcs.get(player.getUniqueId());
        boolean isVisible = visible != null && visible.contains(npc.getId());
        if (!plugin.getVisibilityManager().canSee(player, npc)) {
            if (isVisible)
                hide(player, npc);
            return;
        }

        if (!isVisible) {
            show(player, npc);
            return;
        }

        plugin.getNpcSpawner().destroy(player, npc);
        visible.remove(npc.getId());

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline())
                return;
            if (!plugin.getVisibilityManager().canSee(player, npc))
                return;

            Set<String> set = visibleNpcs.computeIfAbsent(player.getUniqueId(), key -> ConcurrentHashMap.newKeySet());
            if (set.add(npc.getId()))
                plugin.getNpcSpawner().spawn(player, npc);
        }, 2L);
    }

    public void refreshAll(Npc npc) {
        if (npc == null)
            return;

        for (Player player : plugin.getServer().getOnlinePlayers())
            refresh(player, npc);
    }

    public void forget(Player player) {
        if (player == null)
            return;
        visibleNpcs.remove(player.getUniqueId());
    }

    public void forgetHolograms(Player player) {
        forget(player);
    }

    public void shutdown() {
        for (Player player : plugin.getServer().getOnlinePlayers())
            for (Npc npc : plugin.getNpcRegistry().all())
                try {
                    plugin.getNpcSpawner().destroy(player, npc);
                } catch (Throwable ignored) {
                }
        visibleNpcs.clear();
    }
}
