package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.command.CommandSender;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.npc.NpcGlowColor;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

public class SetGlowCommand extends SubCommands {
    @Override
    public String getName() {
        return "setglow";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(ColorUtils.color("&b▶ &fИспользуй: &b/npc setglow [id] [true|false] [color]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        Npc npc = plugin.getNpcRegistry().get(args[1]);
        if (npc == null) {
            sender.sendMessage(ColorUtils.color("&b▶ &fNPC с ID &b" + args[1] + " &fне найден"));
            return;
        }

        boolean glow = Boolean.parseBoolean(args[2]);
        NpcGlowColor color = NpcGlowColor.fromString(args[3]);

        npc.setGlow(glow);
        npc.setGlowColor(color);

        plugin.getConfigManager().save(npc);
        plugin.getNpcManager().refreshAll(npc);

        sender.sendMessage(ColorUtils.color("&b▶ &fВы обновили свечение для NPC: &b" + npc.getId()));
    }
}
