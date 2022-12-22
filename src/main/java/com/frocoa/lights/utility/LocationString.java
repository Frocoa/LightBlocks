package com.frocoa.lights.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class LocationString {

    public static String parseLocationToString(Location location) {
        String coords = location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
        return Objects.requireNonNull(location.getWorld()).getName() + ":" + coords;
    }

    public static Location parseStringToLocation(String locationString) {
        String[] parts = locationString.split(":");
        World w = Bukkit.getServer().getWorld(parts[0]);
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = Integer.parseInt(parts[3]);
        return new Location(w, x, y, z);
    }
}
