package org.zxc_tako25.justnpcs.config;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.npc.NpcAction;
import org.zxc_tako25.justnpcs.npc.NpcGlowColor;
import org.zxc_tako25.justnpcs.npc.NpcHologram;
import org.zxc_tako25.justnpcs.npc.NpcProfession;
import org.zxc_tako25.justnpcs.npc.NpcRegistry;
import org.zxc_tako25.justnpcs.npc.NpcSkin;
import org.zxc_tako25.justnpcs.npc.NpcType;

import java.util.List;

@RequiredArgsConstructor
public final class ConfigManager {
    private static final String NPCS_PATH = "npcs.";
    private final JustNPCs plugin;

    public double viewDistance() {
        return plugin.getConfig().getDouble("settings.view-distance", 48.0);
    }

    public double lineSpacing() {
        return plugin.getConfig().getDouble("settings.hologram-line-spacing", 0.25);
    }

    public double nametagLift() {
        return plugin.getConfig().getDouble("settings.hologram-nametag-lift", 0.30);
    }

    public boolean debug() {
        return plugin.getConfig().getBoolean("settings.debug", false);
    }

    public int metadataOverride(String key) {
        return plugin.getConfig().getInt("settings.metadata." + key, -1);
    }

    public void save(Npc npc) {
        FileConfiguration config = plugin.getConfig();
        String path = NPCS_PATH + npc.getId() + ".";

        config.set(path + "type", npc.getType().name());
        config.set(path + "location", npc.getLocation());
        config.set(path + "skinValue", npc.getSkin().getValue());
        config.set(path + "skinSignature", npc.getSkin().getSignature());
        config.set(path + "hologram", npc.getHologram().snapshot());
        config.set(path + "hologramOffset", npc.getHologram().getOffset());
        config.set(path + "glow", npc.isGlow());
        config.set(path + "glowColor", npc.getGlowColor().name());
        config.set(path + "profession", npc.getProfession().name());
        config.set(path + "commands", npc.getAction().commandsSnapshot());
        config.set(path + "action", npc.getAction().getAction());

        plugin.saveConfig();
    }

    public void delete(String id) {
        plugin.getConfig().set(NPCS_PATH + id, null);
        plugin.saveConfig();
    }

    public void load(NpcRegistry registry) {
        registry.reset();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("npcs");
        if (section == null)
            return;

        for (String id : section.getKeys(false)) {
            Npc npc = loadSingle(id, registry);
            if (npc != null)
                registry.register(npc);
        }
    }

    private Npc loadSingle(String id, NpcRegistry registry) {
        String path = NPCS_PATH + id + ".";

        Location location = plugin.getConfig().getLocation(path + "location");
        if (location == null || location.getWorld() == null)
            return null;

        NpcType type = NpcType.fromString(plugin.getConfig().getString(path + "type", NpcType.PLAYER.name()));
        if (type == null)
            type = NpcType.PLAYER;

        Npc npc = new Npc(id, registry.nextEntityId(), location, type);

        String skinValue = plugin.getConfig().getString(path + "skinValue");
        String skinSignature = plugin.getConfig().getString(path + "skinSignature");
        npc.setSkin(new NpcSkin(skinValue, skinSignature));

        List<String> lines = plugin.getConfig().getStringList(path + "hologram");
        double offset = plugin.getConfig().getDouble(path + "hologramOffset", NpcHologram.DEFAULT_OFFSET);
        NpcHologram hologram = npc.getHologram();
        for (String line : lines)
            hologram.addLine(line);
        hologram.setOffset(offset);

        npc.setGlow(plugin.getConfig().getBoolean(path + "glow"));
        npc.setGlowColor(NpcGlowColor.fromString(plugin.getConfig().getString(path + "glowColor")));
        npc.setProfession(NpcProfession.fromString(plugin.getConfig().getString(path + "profession")));

        NpcAction action = npc.getAction();
        action.setAction(plugin.getConfig().getString(path + "action"));
        for (String command : plugin.getConfig().getStringList(path + "commands"))
            action.addCommand(command);
        return npc;
    }
}
