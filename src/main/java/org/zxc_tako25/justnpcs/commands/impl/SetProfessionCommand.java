package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.command.CommandSender;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.npc.NpcProfession;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

public class SetProfessionCommand extends SubCommands {
    @Override
    public String getName() {
        return "setprofession";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorUtils.color("&b▶ &fИспользуй: &b/npc setprofession [id] [профессия]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        Npc npc = plugin.getNpcRegistry().get(args[1]);
        if (npc == null) {
            sender.sendMessage(ColorUtils.color("&b▶ &fNPC с ID &b" + args[1] + " &fне найден"));
            return;
        }

        if (!npc.isVillagerType()) {
            sender.sendMessage(ColorUtils.color("&b▶ &fПрофессию можно установить только NPC типа &bVILLAGER"));
            return;
        }

        NpcProfession profession = NpcProfession.fromString(args[2]);
        npc.setProfession(profession);

        plugin.getConfigManager().save(npc);
        plugin.getNpcManager().refreshAll(npc);

        sender.sendMessage(ColorUtils.color("&b▶ &fВы установили профисею: &b" + profession.name() + " &fNPC: &b" + npc.getId()));
    }
}
