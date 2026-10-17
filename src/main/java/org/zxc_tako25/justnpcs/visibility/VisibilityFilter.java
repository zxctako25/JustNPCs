package org.zxc_tako25.justnpcs.visibility;

import org.bukkit.entity.Player;
import org.zxc_tako25.justnpcs.npc.Npc;

public interface VisibilityFilter {
    boolean canSee(Player player, Npc npc);
}
