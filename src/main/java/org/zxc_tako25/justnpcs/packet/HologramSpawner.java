package org.zxc_tako25.justnpcs.packet;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.npc.NpcHologram;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public final class HologramSpawner {
    private static final int ID_OFFSET = 1000;
    private static final double PLAYER_HEIGHT = 1.80;
    private static final double VILLAGER_HEIGHT = 1.95;
    private static final LegacyComponentSerializer LINE_SERIALIZER = LegacyComponentSerializer.builder().character('&').hexColors().build();
    private final JustNPCs plugin;
    private final PacketSender sender;
    private final NpcPacketFactory packetFactory;

    public void spawn(Player player, Npc npc) {
        NpcHologram hologram = npc.getHologram();
        if (hologram.isEmpty())
            return;

        Location base = npc.getLocation();
        List<String> lines = hologram.snapshot();
        int count = Math.min(lines.size(), NpcHologram.MAX_LINES);

        for (int index = 0; index < count; index++) {
            String raw = lines.get(index);
            if (raw == null || raw.isEmpty())
                continue;

            int entityId = entityId(npc, index);
            double y = lineY(npc, index, count);

            sender.send(player, new WrapperPlayServerSpawnEntity(
                    entityId,
                    Optional.of(entityUuid(npc, index)),
                    EntityTypes.ARMOR_STAND,
                    new Vector3d(base.getX(), y, base.getZ()),
                    0f, 0f, 0f, 0, Optional.empty()));
            sender.send(player, packetFactory.hologramMetadata(entityId, format(raw)));
        }
    }

    public void update(Player player, Npc npc) {
        NpcHologram hologram = npc.getHologram();
        List<String> lines = hologram.snapshot();
        int count = Math.min(lines.size(), NpcHologram.MAX_LINES);

        List<Integer> toRemove = new ArrayList<>();
        for (int index = count; index < NpcHologram.MAX_LINES; index++)
            toRemove.add(entityId(npc, index));

        for (int index = 0; index < count; index++) {
            String raw = lines.get(index);
            if (raw == null || raw.isEmpty())
                toRemove.add(entityId(npc, index));
        }

        if (!toRemove.isEmpty())
            sender.send(player, new WrapperPlayServerDestroyEntities(toIntArray(toRemove)));
        if (hologram.isEmpty())
            return;

        Location base = npc.getLocation();
        for (int index = 0; index < count; index++) {
            String raw = lines.get(index);
            if (raw == null || raw.isEmpty())
                continue;

            int entityId = entityId(npc, index);
            double y = lineY(npc, index, count);

            sender.send(player, new WrapperPlayServerEntityTeleport(
                    entityId,
                    new Vector3d(base.getX(), y, base.getZ()),
                    0f, 0f, false));
            sender.send(player, packetFactory.hologramLineUpdate(entityId, format(raw)));
        }
    }

    public void destroy(Player player, Npc npc) {
        List<Integer> ids = new ArrayList<>();
        for (int index = 0; index < NpcHologram.MAX_LINES; index++)
            ids.add(entityId(npc, index));

        sender.send(player, new WrapperPlayServerDestroyEntities(toIntArray(ids)));
    }

    private double lineY(Npc npc, int index, int count) {
        double spacing = plugin.getConfigManager().lineSpacing();
        double gap = npc.getHologram().getOffset();
        double feetY = npc.getLocation().getY();
        double headY = feetY + npcHeight(npc);
        double bottomLineY = headY + gap - plugin.getConfigManager().nametagLift();

        int fromBottom = (count - 1) - index;
        return bottomLineY + fromBottom * spacing;
    }

    private double npcHeight(Npc npc) {
        return npc.isPlayerType() ? PLAYER_HEIGHT : VILLAGER_HEIGHT;
    }

    private int entityId(Npc npc, int line) {
        return npc.getEntityId() + ID_OFFSET + line;
    }

    private UUID entityUuid(Npc npc, int line) {
        long seed = line + 1L;
        return new UUID(
                npc.getUuid().getMostSignificantBits() ^ seed,
                npc.getUuid().getLeastSignificantBits() ^ (seed * 31L));
    }

    private Component format(String raw) {
        if (raw == null)
            return Component.empty();
        return LINE_SERIALIZER.deserialize(raw.replace('§', '&'));
    }

    private int[] toIntArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = list.get(i);
        return array;
    }
}
