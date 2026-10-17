package org.zxc_tako25.justnpcs;

import com.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.zxc_tako25.justnpcs.commands.NpcCommand;
import org.zxc_tako25.justnpcs.config.ConfigManager;
import org.zxc_tako25.justnpcs.listener.NpcListener;
import org.zxc_tako25.justnpcs.listener.PacketInteractListener;
import org.zxc_tako25.justnpcs.listener.PlayerConnectionListener;
import org.zxc_tako25.justnpcs.manager.NpcManager;
import org.zxc_tako25.justnpcs.npc.NpcRegistry;
import org.zxc_tako25.justnpcs.packet.HologramSpawner;
import org.zxc_tako25.justnpcs.packet.MetadataResolver;
import org.zxc_tako25.justnpcs.packet.NpcPacketFactory;
import org.zxc_tako25.justnpcs.packet.NpcSpawner;
import org.zxc_tako25.justnpcs.packet.PacketSender;
import org.zxc_tako25.justnpcs.packet.PlayerListManager;
import org.zxc_tako25.justnpcs.packet.TeamManager;
import org.zxc_tako25.justnpcs.task.NpcLookTask;
import org.zxc_tako25.justnpcs.task.NpcTickTask;
import org.zxc_tako25.justnpcs.visibility.VisibilityManager;

@Getter
public class JustNPCs extends JavaPlugin {
    @Getter
    private static JustNPCs instance;
    private ConfigManager configManager;
    private NpcRegistry npcRegistry;
    private VisibilityManager visibilityManager;
    private NpcManager npcManager;
    private PacketSender packetSender;
    private MetadataResolver metadataResolver;
    private NpcPacketFactory npcPacketFactory;
    private HologramSpawner hologramSpawner;
    private NpcSpawner npcSpawner;
    private PacketInteractListener packetInteractListener;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.configManager = new ConfigManager(this);
        this.npcRegistry = new NpcRegistry();
        this.visibilityManager = new VisibilityManager(this);

        this.packetSender = new PacketSender(this);
        this.metadataResolver = new MetadataResolver(this);
        this.npcPacketFactory = new NpcPacketFactory(this);

        TeamManager teamManager = new TeamManager(packetSender);
        PlayerListManager playerListManager = new PlayerListManager(packetSender);

        this.hologramSpawner = new HologramSpawner(this, packetSender, npcPacketFactory);
        this.npcSpawner = new NpcSpawner(packetSender, npcPacketFactory, hologramSpawner, teamManager, playerListManager);

        this.npcManager = new NpcManager(this);

        configManager.load(npcRegistry);

        this.packetInteractListener = new PacketInteractListener(this);
        PacketEvents.getAPI().getEventManager().registerListener(packetInteractListener);

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new NpcListener(this), this);

        new NpcCommand().register(this);
        new NpcTickTask(this).runTaskTimer(this, 20L, 2L);
        new NpcLookTask(this).runTaskTimer(this, 20L, 2L);
    }

    @Override
    public void onDisable() {
        if (packetInteractListener != null)
            PacketEvents.getAPI().getEventManager().unregisterListener(packetInteractListener);
        if (npcManager != null)
            npcManager.shutdown();
    }
}
