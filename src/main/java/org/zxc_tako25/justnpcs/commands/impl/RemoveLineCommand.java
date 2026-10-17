package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.command.CommandSender;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

public class RemoveLineCommand extends SubCommands {
    @Override
    public String getName() {
        return "removeline";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorUtils.color("&b▶ &fИспользуй: &b/npc removeline [id] [index]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        Npc npc = plugin.getNpcRegistry().get(args[1]);
        if (npc == null) {
            sender.sendMessage(ColorUtils.color("&b▶ &fNPC с ID &b" + args[1] + " &fне найден"));
            return;
        }

        int index;
        try {
            index = Integer.parseInt(args[2]);
        } catch (NumberFormatException exception) {
            sender.sendMessage(ColorUtils.color("&b▶ &fИндекс должен быть числом"));
            return;
        }

        if (!npc.getHologram().removeLine(index)) {
            sender.sendMessage(ColorUtils.color("&b▶ &fСтроки с индексом &b" + index + " &fне существует"));
            return;
        }

        plugin.getConfigManager().save(npc);
        plugin.getNpcManager().refreshAll(npc);

        sender.sendMessage(ColorUtils.color("&b▶ &fСтрока &b" + index + " &fудалена"));
    }
}
