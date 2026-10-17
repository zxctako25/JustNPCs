package org.zxc_tako25.justnpcs.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.npc.Npc;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PacketInteractListener extends PacketListenerAbstract {
    private static final long COOLDOWN_MS = 300L;
    private static final long ACTION_DELAY_TICKS = 2L;
    private final JustNPCs plugin;
    private final Map<UUID, Long> cooldown = new ConcurrentHashMap<>();

    public PacketInteractListener(JustNPCs plugin) {
        super(PacketListenerPriority.NORMAL);
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY)
            return;

        WrapperPlayClientInteractEntity interact = new WrapperPlayClientInteractEntity(event);
        WrapperPlayClientInteractEntity.InteractAction action = interact.getAction();

        boolean isRight = action == WrapperPlayClientInteractEntity.InteractAction.INTERACT || action == WrapperPlayClientInteractEntity.InteractAction.INTERACT_AT;
        boolean isLeft = action == WrapperPlayClientInteractEntity.InteractAction.ATTACK;
        if (!isRight && !isLeft)
            return;

        Npc npc = plugin.getNpcRegistry().getByEntityId(interact.getEntityId());
        if (npc == null)
            return;
        if (npc.getAction().isEmpty())
            return;

        Player player = Bukkit.getPlayer(event.getUser().getUUID());
        if (player == null || !player.isOnline())
            return;

        event.setCancelled(true);
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        Long last = cooldown.get(uuid);
        if (last != null && now - last < COOLDOWN_MS)
            return;

        cooldown.put(uuid, now);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline())
                return;

            String action_ = npc.getAction().getAction();
            if (action_ != null && !action_.isEmpty())
                execute(player, action_);

            for (String command : npc.getAction().commandsSnapshot())
                execute(player, command);
        }, ACTION_DELAY_TICKS);
    }

    private void execute(Player player, String raw) {
        if (raw == null || raw.isEmpty())
            return;

        String command = raw.replace("%player%", player.getName());
        if (command.startsWith("/"))
            command = command.substring(1);
        if (command.isEmpty())
            return;

        player.performCommand(command);
    }
}
