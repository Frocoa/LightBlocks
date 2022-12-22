package com.frocoa.lights.model;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class LightBlockTemplate {

    private final List<Long> schedule = new ArrayList<>();
    private final List<Material> materials = new ArrayList<>();
    private final String templateName;

    public LightBlockTemplate(String templateName) {
        this.templateName = templateName;
    }

    public void addSchedule(Long time, Material material) {
        this.schedule.add(time);
        this.materials.add(material);
    }

    public LightBlock createLightBlock(Location location) {
        return new LightBlock(location, schedule, materials, templateName);
    }


}
