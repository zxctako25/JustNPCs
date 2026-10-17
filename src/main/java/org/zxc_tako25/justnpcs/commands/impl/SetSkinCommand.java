package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.command.CommandSender;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.npc.NpcSkin;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

public class SetSkinCommand extends SubCommands {
    @Override
    public String getName() {
        return "setskin";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorUtils.color("&b▶ &fИспользуй &b/npc setskin [id] [value] [signature]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        Npc npc = plugin.getNpcRegistry().get(args[1]);
        if (npc == null) {
            sender.sendMessage(ColorUtils.color("&b▶ &fNPC с ID &b" + args[1] + " &fне найден"));
            return;
        }

        if (!npc.isPlayerType()) {
            sender.sendMessage(ColorUtils.color("&b▶ &fСкин можно установить только NPC типа &bPLAYER"));
            return;
        }

        String value = args[2];
        String signature = args.length >= 4 ? args[3] : null;

        npc.setSkin(new NpcSkin(value, signature));
        plugin.getConfigManager().save(npc);
        plugin.getNpcManager().refreshAll(npc);

        sender.sendMessage(ColorUtils.color("&b▶ &fВы обновили скин для NPC: &b" + npc.getId()));
    }
}
