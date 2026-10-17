package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.command.CommandSender;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

import java.util.Arrays;

public class SetActionCommand extends SubCommands {
    @Override
    public String getName() {
        return "setaction";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorUtils.color("&b▶ &fИспользуй: &b/npc setaction [id] [command]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        Npc npc = plugin.getNpcRegistry().get(args[1]);
        if (npc == null) {
            sender.sendMessage(ColorUtils.color("&b▶ &fNPC с ID &b" + args[1] + " &fне найден"));
            return;
        }

        String command = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        npc.getAction().setAction(command);
        plugin.getConfigManager().save(npc);

        sender.sendMessage(ColorUtils.color("&b▶ &fВы установили команду: &b" + command + " &fдля NPC: &b" + npc.getId()));
    }
}
