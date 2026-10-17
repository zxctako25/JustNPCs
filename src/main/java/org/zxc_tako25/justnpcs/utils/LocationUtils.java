package org.zxc_tako25.justnpcs.utils;

import org.bukkit.Location;

public final class LocationUtils {
    private LocationUtils() {
    }

    public static boolean isSameWorld(Location first, Location second) {
        if (first == null || second == null)
            return false;
        if (first.getWorld() == null || second.getWorld() == null)
            return false;
        return first.getWorld().equals(second.getWorld());
    }

    public static boolean isWithinDistance(Location from, Location to, double distance) {
        if (!isSameWorld(from, to))
            return false;
        return from.distanceSquared(to) <= distance * distance;
    }

    public static Location center(Location location) {
        Location copy = location.clone();
        copy.setX(copy.getBlockX() + 0.5);
        copy.setZ(copy.getBlockZ() + 0.5);
        return copy;
    }
}
