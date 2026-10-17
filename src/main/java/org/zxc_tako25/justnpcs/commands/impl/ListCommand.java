package org.zxc_tako25.justnpcs.commands.impl;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.SubCommands;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

public class ListCommand extends SubCommands {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        JustNPCs plugin = JustNPCs.getInstance();

        if (plugin.getNpcRegistry().isEmpty()) {
            sender.sendMessage(ColorUtils.color("&b▶ &fСписок NPC пуст"));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(ColorUtils.color("&b▶ &fСписок активных NPC:"));

        for (Npc npc : plugin.getNpcRegistry().all()) {
            Location location = npc.getLocation();
            String world = location.getWorld() != null ? location.getWorld().getName() : "unknown";
            String coords = location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();

            sender.sendMessage(ColorUtils.color("&b&l&n▍&f ID: &b" + npc.getId()));
            sender.sendMessage(ColorUtils.color("&b&l&n▍&f Мир: &b" + world));
            sender.sendMessage(ColorUtils.color("&b&l&n▍&f Координаты: &b" + coords));
            sender.sendMessage(ColorUtils.color("&b&l&n▍&f Тип: &b" + npc.getType().name()));
            sender.sendMessage(ColorUtils.color("&b&l▍&f Профессия: &b" + npc.getProfession().name()));
            sender.sendMessage(ColorUtils.color(""));
        }
    }
}
