package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

public class RemoveCommand extends SubCommands {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorUtils.color("&b▶ &fИпользуй: &b/npc remove [id]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        Npc npc = plugin.getNpcRegistry().get(args[1]);
        if (npc == null) {
            sender.sendMessage(ColorUtils.color("&b▶ &fNPC с ID &b" + args[1] + " &fне найден"));
            return;
        }

        for (Player online : plugin.getServer().getOnlinePlayers())
            plugin.getNpcManager().hide(online, npc);

        plugin.getNpcRegistry().remove(npc.getId());
        plugin.getConfigManager().delete(npc.getId());

        sender.sendMessage(ColorUtils.color("&b▶ &fВы удалили NPC: &b" + npc.getId()));
    }
}
