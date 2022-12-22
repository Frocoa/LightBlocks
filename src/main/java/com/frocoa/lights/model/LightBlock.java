package com.frocoa.lights.model;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class LightBlock {
    private final Location location;
    private final List<Long> schedule;
    private final List<Material> materials;
    private final String templateName;

    public LightBlock(Location location, List<Long> schedule, List<Material> materials, String templateName) {
        this.location = location;
        this.schedule = schedule;
        this.materials = materials;
        this.templateName = templateName;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getTemplateName() {
        return templateName;
    }

    public Material getCurrentMaterial(Long current_time) {
        int index = 0;
        long min_delta = 25000L;
        Material current_material = Material.AIR;
        for (Long time: schedule) {
            long delta = current_time - time;
            if (delta >= 0 && delta <= min_delta) {
                current_material = materials.get(index);
                min_delta = delta;
            }
            index++;
        }
        return current_material;
    }
}
