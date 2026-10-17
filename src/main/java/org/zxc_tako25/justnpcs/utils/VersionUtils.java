package org.zxc_tako25.justnpcs.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;

public final class VersionUtils {

    private VersionUtils() {
    }

    public static ServerVersion current() {
        return PacketEvents.getAPI().getServerManager().getVersion();
    }

    public static boolean atLeast(ServerVersion version) {
        return current().isNewerThanOrEquals(version);
    }

    public static boolean isLegacy() {
        return current().isOlderThan(ServerVersion.V_1_17);
    }
}
