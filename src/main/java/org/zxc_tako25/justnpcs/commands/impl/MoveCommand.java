package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

public class MoveCommand extends SubCommands {
    @Override
    public String getName() {
        return "move";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player))
            return;

        if (args.length < 2) {
            player.sendMessage(ColorUtils.color("&b▶ &fИспользуй: &b/npc move [id]"));
            return;
        }

        JustNPCs plugin = JustNPCs.getInstance();
        Npc npc = plugin.getNpcRegistry().get(args[1]);
        if (npc == null) {
            sender.sendMessage(ColorUtils.color("&b▶ &fNPC с ID &b" + args[1] + " &fне найден"));
            return;
        }

        npc.setLocation(player.getLocation());
        plugin.getConfigManager().save(npc);
        plugin.getNpcManager().refreshAll(npc);

        player.sendMessage(ColorUtils.color("&b▶ &fВы переместили NPC: &b" + npc.getId() + " &fна вашу позицию"));
    }
}
