package org.zxc_tako25.justnpcs.packet;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.JustNPCs;

@RequiredArgsConstructor
public final class PacketSender {
    private final JustNPCs plugin;

    public void send(Player player, PacketWrapper<?> packet) {
        if (player == null || !player.isOnline())
            return;

        try {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        } catch (Throwable throwable) {
            if (plugin.getConfigManager().debug())
                plugin.getLogger().warning("Не удалось отправить пакет " + packet.getClass().getSimpleName() + " игроку " + player.getName() + ": " + throwable.getMessage());
        }
    }
}
