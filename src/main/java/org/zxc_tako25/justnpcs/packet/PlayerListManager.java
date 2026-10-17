package org.zxc_tako25.justnpcs.packet;

import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.utils.VersionUtils;

import java.util.EnumSet;

@RequiredArgsConstructor
public final class PlayerListManager {
    private final PacketSender sender;

    public void add(Player player, Npc npc, UserProfile profile) {
        if (VersionUtils.atLeast(com.github.retrooper.packetevents.manager.server.ServerVersion.V_1_19_3)) {
            WrapperPlayServerPlayerInfoUpdate.PlayerInfo info = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                            profile, false, 0, GameMode.SURVIVAL, Component.empty(), null);
            sender.send(player, new WrapperPlayServerPlayerInfoUpdate(
                    EnumSet.of(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER), info));
        } else {
            WrapperPlayServerPlayerInfo.PlayerData data = new WrapperPlayServerPlayerInfo.PlayerData(
                            Component.text(profile.getName()), profile, GameMode.SURVIVAL, 0);
            sender.send(player, new WrapperPlayServerPlayerInfo(
                    WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, data));
        }
    }

    public void remove(Player player, Npc npc) {
        if (VersionUtils.atLeast(com.github.retrooper.packetevents.manager.server.ServerVersion.V_1_19_3)) {
            sender.send(player, new WrapperPlayServerPlayerInfoRemove(npc.getUuid()));
        } else {
            WrapperPlayServerPlayerInfo.PlayerData data = new WrapperPlayServerPlayerInfo.PlayerData(
                            Component.text(npc.getId()),
                            new UserProfile(npc.getUuid(), npc.getId()),
                            GameMode.SURVIVAL, 0);
            sender.send(player, new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, data));
        }
    }
}
