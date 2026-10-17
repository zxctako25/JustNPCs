package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.command.CommandSender;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.npc.NpcHologram;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

import java.util.Arrays;

public class AddLineCommand extends SubCommands {
    @Override
    public String getName() {
        return "addline";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorUtils.color("&b▶ &fИспользуй: &b/npc addline [id] [текст]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        Npc npc = plugin.getNpcRegistry().get(args[1]);
        if (npc == null) {
            sender.sendMessage(ColorUtils.color("&b▶ &fNPC с ID &b" + args[1] + " &fне найден"));
            return;
        }

        if (npc.getHologram().size() >= NpcHologram.MAX_LINES) {
            sender.sendMessage(ColorUtils.color("&b▶ &fДостигнут лимит строк: &b" + NpcHologram.MAX_LINES));
            return;
        }

        String line = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        npc.getHologram().addLine(line);

        plugin.getConfigManager().save(npc);
        plugin.getNpcManager().refreshAll(npc);
    }
}
