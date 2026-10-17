package org.zxc_tako25.justnpcs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.commands.impl.*;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.npc.NpcGlowColor;
import org.zxc_tako25.justnpcs.npc.NpcProfession;
import org.zxc_tako25.justnpcs.npc.NpcType;
import org.zxc_tako25.justnpcs.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class NpcCommand implements CommandExecutor, TabCompleter {
    private final List<SubCommands> subCommands = new ArrayList<>();

    public NpcCommand() {
        subCommands.add(new AddLineCommand());
        subCommands.add(new ClearActionCommand());
        subCommands.add(new CreateCommand());
        subCommands.add(new ListCommand());
        subCommands.add(new MoveCommand());
        subCommands.add(new RemoveCommand());
        subCommands.add(new RemoveLineCommand());
        subCommands.add(new SetActionCommand());
        subCommands.add(new SetGlowCommand());
        subCommands.add(new SetProfessionCommand());
        subCommands.add(new SetSkinCommand());
        subCommands.add(new TpCommand());
    }

    public void register(JustNPCs plugin) {
        plugin.getCommand("npc").setExecutor(this);
        plugin.getCommand("npc").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        for (SubCommands subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(args[0])) {
                subCommand.execute(sender, args);
                return true;
            }
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc create [id] [тип] &f- создать НПС"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc remove [id] &f- удалить НПС"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc list &f- посмотреть список всех НПС"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc tp [id] &f- телепортироватся к НПС"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc move [id] &f- переместить нпс к себе"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc setglow [id] [true|false] [color] &f- установить свечение НПС"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc setskin [id] [value] [signature] &f- установить скинь НПС"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc setprofession [id] [profession] &f- установить профессию НПС"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc setaction [id] [command] &f- задать команду НПС"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc addline [id] [text] &f- добавить линию в голограмму"));
        sender.sendMessage(ColorUtils.color("&b&l&n▍&b /npc removeline [id] [index] &f- удалить линию из голограммы"));
        sender.sendMessage(ColorUtils.color("&b&l▍&b /npc clearaction [id] &f- очистить все команды для НПС"));
        sender.sendMessage("");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.add("create");
            list.add("remove");
            list.add("list");
            list.add("tp");
            list.add("move");
            list.add("setglow");
            list.add("setskin");
            list.add("setprofession");
            list.add("setaction");
            list.add("addline");
            list.add("removeline");
            list.add("clearaction");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")
                    || args[0].equalsIgnoreCase("tp")
                    || args[0].equalsIgnoreCase("move")
                    || args[0].equalsIgnoreCase("setglow")
                    || args[0].equalsIgnoreCase("setskin")
                    || args[0].equalsIgnoreCase("setprofession")
                    || args[0].equalsIgnoreCase("setaction")
                    || args[0].equalsIgnoreCase("addline")
                    || args[0].equalsIgnoreCase("removeline")
                    || args[0].equalsIgnoreCase("clearaction")) {
                for (Npc npc : JustNPCs.getInstance().getNpcRegistry().all())
                    list.add(npc.getId());
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                for (NpcType type : NpcType.values())
                    list.add(type.name());
            } else if (args[0].equalsIgnoreCase("setglow")) {
                list.add("true");
                list.add("false");
            } else if (args[0].equalsIgnoreCase("setprofession")) {
                for (String profession : NpcProfession.names())
                    list.add(profession);
            } else if (args[0].equalsIgnoreCase("removeline")) {
                Npc npc = JustNPCs.getInstance().getNpcRegistry().get(args[1]);
                if (npc != null)
                    for (int index = 0; index < npc.getHologram().size(); index++)
                        list.add(String.valueOf(index));
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("setglow"))
                for (NpcGlowColor color : NpcGlowColor.values())
                    list.add(color.name());
        }

        String prefix = args[args.length - 1].toLowerCase();
        List<String> result = new ArrayList<>();

        for (String value : list)
            if (value.toLowerCase().startsWith(prefix))
                result.add(value);
        return result;
    }
}
