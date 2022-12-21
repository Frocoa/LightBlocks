package com.frocoa.lights.model;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class LightBlock {
    private final Location location;
    private final List<Long> schedule;
    private final List<Material> materials ;

    public LightBlock(Location location, List<Long> schedule, List<Material> materials) {
        this.location = location;
        this.schedule = schedule;
        this.materials = materials;
    }

    public Location getLocation() {
        return this.location;
    }

    public Material getCurrentMaterial(Long current_time) {
        int index = 0;
        long min_delta = 25000L;
        Material current_material = Material.AIR;
        for (Long time: schedule) {
            long delta = current_time - time;
            System.out.println("Potential delta: " + delta + " for material " + materials.get(index));
            if (delta >= 0 && delta <= min_delta) {
                System.out.println("New min_delta: " + delta + " for material " + materials.get(index));
                current_material = materials.get(index);
                min_delta = delta;
            }
            index++;
        }
        System.out.println("Final material selected: " + current_material);
        return current_material;
    }
}
