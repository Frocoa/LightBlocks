package com.frocoa.lights.model;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LightBlockTemplate {

    private final List<Long> schedule = new ArrayList<>();
    private final List<BlockClosure> blocks = new ArrayList<>();
    private final String templateName;

    public LightBlockTemplate(String templateName) {
        this.templateName = templateName;
    }

    public void addSchedule(Long time, Material material) {
        this.schedule.add(time);
        this.blocks.add(new BlockClosure(material, false));
    }

    public void addSchedule(Long time, Material material, boolean lit) {
        this.schedule.add(time);
        this.blocks.add(new BlockClosure(material, lit));
    }

    public LightBlock createLightBlock(Location location, String player) {
        return new LightBlock(location, schedule, blocks, templateName, player);
    }


}
