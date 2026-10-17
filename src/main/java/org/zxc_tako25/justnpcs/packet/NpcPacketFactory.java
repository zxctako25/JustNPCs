package org.zxc_tako25.justnpcs.packet;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfession;
import com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfessions;
import com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerTypes;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPlayer;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.zxc_tako25.justnpcs.JustNPCs;
import org.zxc_tako25.justnpcs.npc.Npc;
import org.zxc_tako25.justnpcs.npc.NpcProfession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public final class NpcPacketFactory {
    private static final byte FLAG_GLOWING = 0x40;
    private static final byte FLAG_INVISIBLE = 0x20;
    private static final byte SKIN_PARTS_ALL = 0x7F;
    private final JustNPCs plugin;

    public WrapperPlayServerSpawnPlayer playerSpawn(Npc npc) {
        Location location = npc.getLocation();
        UserProfile profile = buildProfile(npc);

        return new WrapperPlayServerSpawnPlayer(
                npc.getEntityId(),
                npc.getUuid(),
                new Vector3d(location.getX(), location.getY(), location.getZ()),
                location.getYaw(),
                location.getPitch(),
                Collections.emptyList());
    }

    public UserProfile buildProfile(Npc npc) {
        UserProfile profile = new UserProfile(npc.getUuid(), npc.getId());
        if (npc.getSkin().isValid() && npc.getSkin().hasSignature())
            profile.setTextureProperties(Collections.singletonList(new TextureProperty("textures", npc.getSkin().getValue(), npc.getSkin().getSignature())));
        return profile;
    }

    public WrapperPlayServerSpawnEntity villagerSpawn(Npc npc) {
        Location location = npc.getLocation();

        return new WrapperPlayServerSpawnEntity(
                npc.getEntityId(),
                Optional.of(npc.getUuid()),
                EntityTypes.VILLAGER,
                new Vector3d(location.getX(), location.getY(), location.getZ()),
                location.getPitch(),
                location.getYaw(),
                location.getYaw(),
                0,
                Optional.empty());
    }

    public WrapperPlayServerEntityMetadata playerSpawnMetadata(Npc npc) {
        List<EntityData<?>> data = new ArrayList<>(4);
        data.add(new EntityData<>(0, EntityDataTypes.BYTE, flags(npc)));
        data.add(new EntityData<>(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT, Optional.<Component>empty()));
        data.add(new EntityData<>(3, EntityDataTypes.BOOLEAN, false));
        data.add(new EntityData<>(plugin.getMetadataResolver().playerSkinPartsIndex(), EntityDataTypes.BYTE, SKIN_PARTS_ALL));
        return new WrapperPlayServerEntityMetadata(npc.getEntityId(), data);
    }

    public WrapperPlayServerEntityMetadata villagerSpawnMetadata(Npc npc) {
        List<EntityData<?>> data = new ArrayList<>(6);
        data.add(new EntityData<>(0, EntityDataTypes.BYTE, flags(npc)));
        data.add(new EntityData<>(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT, Optional.<Component>empty()));
        data.add(new EntityData<>(3, EntityDataTypes.BOOLEAN, false));
        data.add(new EntityData<>(4, EntityDataTypes.BOOLEAN, true));
        data.add(new EntityData<>(5, EntityDataTypes.BOOLEAN, true));

        VillagerData villagerData = buildVillagerData(npc);
        if (villagerData != null)
            data.add(new EntityData<>(plugin.getMetadataResolver().villagerDataIndex(), EntityDataTypes.VILLAGER_DATA, villagerData));
        return new WrapperPlayServerEntityMetadata(npc.getEntityId(), data);
    }

    public WrapperPlayServerEntityMetadata updateMetadata(Npc npc) {
        List<EntityData<?>> data = new ArrayList<>(3);
        data.add(new EntityData<>(0, EntityDataTypes.BYTE, flags(npc)));

        if (npc.isPlayerType()) {
            data.add(new EntityData<>(plugin.getMetadataResolver().playerSkinPartsIndex(), EntityDataTypes.BYTE, SKIN_PARTS_ALL));
        } else {
            VillagerData villagerData = buildVillagerData(npc);
            if (villagerData != null)
                data.add(new EntityData<>(plugin.getMetadataResolver().villagerDataIndex(), EntityDataTypes.VILLAGER_DATA, villagerData));
        }
        return new WrapperPlayServerEntityMetadata(npc.getEntityId(), data);
    }

    public WrapperPlayServerEntityMetadata hologramMetadata(int entityId, Component component) {
        List<EntityData<?>> data = new ArrayList<>(6);
        data.add(new EntityData<>(0, EntityDataTypes.BYTE, FLAG_INVISIBLE));
        data.add(new EntityData<>(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT, Optional.of(component)));
        data.add(new EntityData<>(3, EntityDataTypes.BOOLEAN, true));
        data.add(new EntityData<>(4, EntityDataTypes.BOOLEAN, true));
        data.add(new EntityData<>(5, EntityDataTypes.BOOLEAN, true));
        data.add(new EntityData<>(plugin.getMetadataResolver().armorStandFlagsIndex(), EntityDataTypes.BYTE, (byte) 0x11));
        return new WrapperPlayServerEntityMetadata(entityId, data);
    }

    public WrapperPlayServerEntityMetadata hologramLineUpdate(int entityId, Component component) {
        List<EntityData<?>> data = new ArrayList<>(1);
        data.add(new EntityData<>(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT, Optional.of(component)));
        return new WrapperPlayServerEntityMetadata(entityId, data);
    }

    private byte flags(Npc npc) {
        return npc.isGlow() ? FLAG_GLOWING : (byte) 0x00;
    }

    private VillagerData buildVillagerData(Npc npc) {
        try {
            return new VillagerData(VillagerTypes.PLAINS, resolveProfession(npc.getProfession()), 2);
        } catch (Throwable throwable) {
            plugin.getLogger().warning("Не удалось создать VillagerData для NPC " + npc.getId() + ": " + throwable.getMessage());
            return null;
        }
    }

    private VillagerProfession resolveProfession(NpcProfession profession) {
        switch (profession) {
            case ARMORER:
                return VillagerProfessions.ARMORER;
            case BUTCHER:
                return VillagerProfessions.BUTCHER;
            case CARTOGRAPHER:
                return VillagerProfessions.CARTOGRAPHER;
            case CLERIC:
                return VillagerProfessions.CLERIC;
            case FARMER:
                return VillagerProfessions.FARMER;
            case FISHERMAN:
                return VillagerProfessions.FISHERMAN;
            case FLETCHER:
                return VillagerProfessions.FLETCHER;
            case LEATHERWORKER:
                return VillagerProfessions.LEATHERWORKER;
            case LIBRARIAN:
                return VillagerProfessions.LIBRARIAN;
            case MASON:
                return VillagerProfessions.MASON;
            case NITWIT:
                return VillagerProfessions.NITWIT;
            case SHEPHERD:
                return VillagerProfessions.SHEPHERD;
            case TOOLSMITH:
                return VillagerProfessions.TOOLSMITH;
            case WEAPONSMITH:
                return VillagerProfessions.WEAPONSMITH;
            case NONE:
            default:
                return VillagerProfessions.NONE;
        }
    }
}
