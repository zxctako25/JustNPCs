package org.zxc_tako25.justnpcs.npc;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class NpcRegistry {
    private static final int ID_BASE = 1_000_000;
    private static final int ID_STEP = 64;
    private final Map<String, Npc> npcs = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(ID_BASE);

    public int nextEntityId() {
        return idCounter.getAndAdd(ID_STEP);
    }

    public void reset() {
        npcs.clear();
        idCounter.set(ID_BASE);
    }

    public void register(Npc npc) {
        npcs.put(npc.getId(), npc);
    }

    public Npc remove(String id) {
        return npcs.remove(id);
    }

    public Npc get(String id) {
        return npcs.get(id);
    }

    public Npc getByEntityId(int entityId) {
        for (Npc npc : npcs.values())
            if (npc.getEntityId() == entityId)
                return npc;
        return null;
    }

    public boolean contains(String id) {
        return npcs.containsKey(id);
    }

    public Collection<Npc> all() {
        return Collections.unmodifiableCollection(npcs.values());
    }

    public boolean isEmpty() {
        return npcs.isEmpty();
    }
}
