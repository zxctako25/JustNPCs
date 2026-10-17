package org.zxc_tako25.justnpcs.packet;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.npc.Npc;

import java.util.Collections;

@RequiredArgsConstructor
public final class TeamManager {
    private final PacketSender sender;

    public void ensure(Player player, Npc npc) {
        String teamName = teamName(npc);
        String entry = entry(npc);
        NamedTextColor color = npc.isGlow() ? npc.getGlowColor().getColor() : NamedTextColor.WHITE;

        WrapperPlayServerTeams.ScoreBoardTeamInfo info = new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                Component.text(teamName),
                Component.empty(),
                Component.empty(),
                WrapperPlayServerTeams.NameTagVisibility.NEVER,
                WrapperPlayServerTeams.CollisionRule.NEVER,
                color,
                WrapperPlayServerTeams.OptionData.NONE);

        sender.send(player, new WrapperPlayServerTeams(
                teamName, WrapperPlayServerTeams.TeamMode.CREATE, info, Collections.singletonList(entry)));

        sender.send(player, new WrapperPlayServerTeams(
                teamName, WrapperPlayServerTeams.TeamMode.ADD_ENTITIES,
                (WrapperPlayServerTeams.ScoreBoardTeamInfo) null, Collections.singletonList(entry)));
    }

    public void remove(Player player, Npc npc) {
        sender.send(player, new WrapperPlayServerTeams(
                teamName(npc), WrapperPlayServerTeams.TeamMode.REMOVE,
                (WrapperPlayServerTeams.ScoreBoardTeamInfo) null,
                Collections.singletonList(entry(npc))));
    }

    private String teamName(Npc npc) {
        String raw = "jn" + Math.abs(npc.getEntityId() % 1_000_000);
        return raw.length() > 16 ? raw.substring(0, 16) : raw;
    }

    private String entry(Npc npc) {
        return npc.isPlayerType() ? npc.getId() : npc.getUuid().toString();
    }
}
