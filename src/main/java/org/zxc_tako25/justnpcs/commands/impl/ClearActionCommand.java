package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.command.CommandSender;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

public class ClearActionCommand extends SubCommands {
    @Override
    public String getName() {
        return "clearaction";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorUtils.color("&b▶ &fИспользуй: &b/npc clearaction [id]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        Npc npc = plugin.getNpcRegistry().get(args[1]);
        if (npc == null) {
            sender.sendMessage(ColorUtils.color("&b▶ &fNPC с ID &b" + args[1] + " &fне найден"));
            return;
        }

        npc.getAction().clear();
        plugin.getConfigManager().save(npc);

        sender.sendMessage(ColorUtils.color("&b▶ &fВы очистили все команды для NPC: &b" + npc.getId()));
    }
}
