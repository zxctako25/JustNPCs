package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.npc.NpcType;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

public class CreateCommand extends SubCommands {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player))
            return;

        if (args.length < 3) {
            player.sendMessage(ColorUtils.color("&b▶ &fИспользуй: &b/npc create [id] [тип]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        String id = args[1];

        if (plugin.getNpcRegistry().contains(id)) {
            player.sendMessage(ColorUtils.color("&b▶ &fNPC: &b" + id + " &fуже существует"));
            return;
        }

        NpcType type = NpcType.fromString(args[2]);
        if (type == null) {
            player.sendMessage(ColorUtils.color("&b▶ &fНеверный тип"));
            return;
        }

        Location location = player.getLocation();
        Npc npc = new Npc(id, plugin.getNpcRegistry().nextEntityId(), location, type);

        plugin.getNpcRegistry().register(npc);
        plugin.getConfigManager().save(npc);
        for (Player online : plugin.getServer().getOnlinePlayers())
            if (plugin.getVisibilityManager().canSee(online, npc))
                plugin.getNpcManager().show(online, npc);

        player.sendMessage(ColorUtils.color("&b▶ &fВы создали NPC: &b" + npc.getId() + " &fс типом &b" + type.name()));
    }
}
