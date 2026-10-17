package org.zxc_tako25.justnpcs.packet;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityHeadLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.npc.Npc;

@RequiredArgsConstructor
public final class NpcSpawner {
    private final PacketSender sender;
    private final NpcPacketFactory packetFactory;
    private final HologramSpawner hologramSpawner;
    private final TeamManager teamManager;
    private final PlayerListManager playerListManager;

    public void spawn(Player player, Npc npc) {
        if (npc.isPlayerType()) {
            playerListManager.add(player, npc, packetFactory.buildProfile(npc));
            sender.send(player, packetFactory.playerSpawn(npc));
            sender.send(player, packetFactory.playerSpawnMetadata(npc));
        } else {
            sender.send(player, packetFactory.villagerSpawn(npc));
            sender.send(player, packetFactory.villagerSpawnMetadata(npc));
        }

        sender.send(player, new WrapperPlayServerEntityHeadLook(npc.getEntityId(), npc.getLocation().getYaw()));

        hologramSpawner.spawn(player, npc);
        teamManager.ensure(player, npc);
    }

    public void destroy(Player player, Npc npc) {
        hologramSpawner.destroy(player, npc);
        teamManager.remove(player, npc);
        if (npc.isPlayerType())
            playerListManager.remove(player, npc);

        sender.send(player, new WrapperPlayServerDestroyEntities(npc.getEntityId()));
    }

    public void lookAt(Player player, Npc npc, float yaw, float pitch) {
        sender.send(player, new WrapperPlayServerEntityHeadLook(npc.getEntityId(), yaw));
        sender.send(player, new WrapperPlayServerEntityRotation(npc.getEntityId(), yaw, pitch, true));
    }
}
